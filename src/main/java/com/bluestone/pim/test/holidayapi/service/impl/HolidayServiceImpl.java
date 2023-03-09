package com.bluestone.pim.test.holidayapi.service.impl;

import com.bluestone.pim.test.holidayapi.client.HolidayClient;
import com.bluestone.pim.test.holidayapi.exception.ResponseException;
import com.bluestone.pim.test.holidayapi.service.HolidayService;
import com.bluestone.pim.test.holidayapi.model.HolidayNagerResponse;
import com.bluestone.pim.test.holidayapi.model.HolidayResponse;
import com.bluestone.pim.test.holidayapi.util.RequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    HolidayClient holidayClient;

    @Override
    public HolidayResponse getNextHolidaySameDay(String date, String code1, String code2) throws ResponseException{

        if(!RequestValidator.isValidDate(date))
            throw new ResponseException("Invalid date format", HttpStatus.BAD_REQUEST.value());

        if(!RequestValidator.isValidISOCountry(code1) || !RequestValidator.isValidISOCountry(code2))
            throw new ResponseException("Invalid countryCode format", HttpStatus.BAD_REQUEST.value());

        LocalDate localDate = LocalDate.parse(date);
        String year = String.valueOf(localDate.getYear());

        Mono<List<HolidayNagerResponse>> listCountry1 = holidayClient.findHolidayByYearAndCountry(year, code1);
        Mono<List<HolidayNagerResponse>> listCountry2 = holidayClient.findHolidayByYearAndCountry(year, code2);

        log.info("List object from mono 1[{}]", listCountry1.block());
        log.info("List object from mono 2[{}]", listCountry2.block());

        Mono<Map<LocalDate, List<String>>> list = Flux.merge(listCountry1,listCountry2)
                .flatMapIterable(Function.identity())
                .collect(Collectors.groupingBy(
                            HolidayNagerResponse::getDate,
                            Collectors.mapping(
                                    HolidayNagerResponse::getName,
                                    Collectors.toList()
                        )));

        Map<LocalDate, List<String>> listMap = list.block();
        log.info("Map object extracted from merging and groupingBy Mono [{}]", listMap);

        log.info("Ordering map object by LocalDate");
        Map<LocalDate, List<String>> orderedByDate = new TreeMap<>(listMap);

        HolidayResponse holidayResponse = new HolidayResponse();

        log.info("Iterating over ordered map to retrieve first key/value that match condition");

        Optional<Map.Entry<LocalDate, List<String>>> optional = orderedByDate.entrySet().stream()
                        .filter(f->f.getKey().isAfter(localDate) && f.getValue().size()>1)
                        .findFirst();

        if(optional.isPresent()) {
            holidayResponse.setDate(optional.get().getKey());
            holidayResponse.setName1(optional.get().getValue().get(0));
            holidayResponse.setName2(optional.get().getValue().get(1));
        }

        if(holidayResponse.getName1() == null || holidayResponse.getName1().isEmpty())
            throw new ResponseException("Resource not found", HttpStatus.NOT_FOUND.value());

        return holidayResponse;
    }

}
