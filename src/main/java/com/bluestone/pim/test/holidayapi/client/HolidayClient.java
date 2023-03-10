package com.bluestone.pim.test.holidayapi.client;

import com.bluestone.pim.test.holidayapi.model.HolidayExternalApiResponse;
import com.bluestone.pim.test.holidayapi.util.PropertiesReader;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.List;

import static org.springframework.http.MediaType.TEXT_PLAIN;

/**
 * The class Holiday Client (Custom Web Client)
 */
@Service
@Slf4j
public class HolidayClient {

    /**
     * WebClient object to hold injected dependency
     *
     * @var webClient
     */
    WebClient webClient;

    /**
     * Default constructor method to HolidayClient
     *
     * @param builder WebClient.Builder object
     */
    public HolidayClient(WebClient.Builder builder) throws SSLException {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        webClient = builder
                .clientConnector(new ReactorClientHttpConnector(
                                HttpClient.create()
                                        .secure(t -> t.sslContext(sslContext))
                                        .resolver(DefaultAddressResolverGroup.INSTANCE)
                        )
                )
                .baseUrl(PropertiesReader.getProperty("BASE_NAGER_API_URL"))
                .filter(WebClientFilter.logRequest())
                .filter(WebClientFilter.logResponse())
                .filter(WebClientFilter.handleError())
                .build();
    }

    /**
     * Method findHolidayByYearAndCountry
     * Responsible to retrieve data from external API
     *
     * @param year String year
     * @param code String country code
     * @return Return Mono object
     */
    public Mono<List<HolidayExternalApiResponse>> findHolidayByYearAndCountry(String year, String code) {
        log.info("Searching holidays in the year of [{}] for the country [{}]", year, code);

        return webClient
                .get()
                .uri("/publicholidays/" + year + "/" + code)
                .accept(TEXT_PLAIN)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Verify informed parameters")))
                .bodyToMono(new ParameterizedTypeReference<List<HolidayExternalApiResponse>>() {});
    }

    /**
     * Method findHolidayByYearAndCountryOTHER_SERVICE not used due to limitations on the external API free plan
     * Responsible to retrieve data from external API
     *
     * @param year String year
     * @param code String country code
     * @return Return Mono object
     */
    public Mono<List<HolidayExternalApiResponse>> findHolidayByYearAndCountryOTHER_SERVICE(String year, String code) {
        log.info("Searching holidays in the year of [{}] for the country [{}]", year, code);

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("api-key", PropertiesReader.getProperty("APP_KEY"))
                        .queryParam("country", code)
                        .queryParam("year", year)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Verify informed parameters")))
                .bodyToMono(new ParameterizedTypeReference<List<HolidayExternalApiResponse>>() {});
    }

}
