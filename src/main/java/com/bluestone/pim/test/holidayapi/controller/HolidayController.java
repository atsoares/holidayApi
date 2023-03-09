package com.bluestone.pim.test.holidayapi.controller;

import com.bluestone.pim.test.holidayapi.exception.ResponseException;
import com.bluestone.pim.test.holidayapi.model.HolidayResponse;
import com.bluestone.pim.test.holidayapi.service.HolidayService;

import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.bluestone.pim.test.holidayapi.constants.Constants.*;

@RestController
@RequestMapping(path = "/api",
        produces = "application/json")
@CrossOrigin(origins = "*")
@Slf4j
public class HolidayController {

    @Autowired
    private HolidayService service;

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = STATUS_200_GET_OK, response = HolidayResponse.class),
                    @ApiResponse(code = 400, message = STATUS_400_BAD_REQUEST),
                    @ApiResponse(code = 404, message = STATUS_404_NOT_FOUND)
            })
    @GetMapping("/holiday")
    public ResponseEntity<Object> getNextHolidaySameDay(
            @ApiParam(value = "date", required = true, defaultValue = "2022-03-04") @RequestParam String date,
            @ApiParam(value = "countryCode1", required = true, defaultValue = "PL") @RequestParam String countryCode1,
            @ApiParam(value = "countryCode2", required = true, defaultValue = "BR") @RequestParam String countryCode2)
            throws ResponseException{
        log.info("Verifying next holiday that happens after date {} for countries [{}, {}]",
                date, countryCode1, countryCode2);

        try {
            return new ResponseEntity<>(service.getNextHolidaySameDay(date, countryCode1, countryCode2), HttpStatus.OK);
        }
        catch (ResponseException exc) {
            log.error("Exception catch: Status [{}]; Message[{}]", exc.getStatusCode(), exc.getMessage());
            throw new ResponseStatusException(
                    exc.getStatusCode(), exc.getMessage(), exc);
        }

    }

}
