package com.bluestone.pim.test.holidayapi.client;

import com.bluestone.pim.test.holidayapi.model.HolidayNagerResponse;
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

@Service
@Slf4j
public class HolidayClient {

    WebClient webClient;
    private static final String BASE_API_URL = "https://date.nager.at/api/v3/";

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
                .baseUrl(BASE_API_URL)
                .filter(WebClientFilter.logRequest())
                .filter(WebClientFilter.logResponse())
                .filter(WebClientFilter.handleError())
                .build();
    }

    public Mono<List<HolidayNagerResponse>> findHolidayByYearAndCountry(String year, String code) {
        log.info("Searching holidays in the year of [{}] for the country [{}]", year, code);

        return webClient
                .get()
                .uri("/publicholidays/" + year + "/" + code)
                .accept(TEXT_PLAIN)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Verify informed parameters")))
                .bodyToMono(new ParameterizedTypeReference<List<HolidayNagerResponse>>() {});
    }

}
