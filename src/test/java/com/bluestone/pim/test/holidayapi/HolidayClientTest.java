package com.bluestone.pim.test.holidayapi;

import com.bluestone.pim.test.holidayapi.client.HolidayClient;
import com.bluestone.pim.test.holidayapi.model.HolidayNagerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import javax.net.ssl.SSLException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import({HolidayClientTest.Config.class, JacksonAutoConfiguration.class})
@ExtendWith(SpringExtension.class)
public class HolidayClientTest {

    @TestConfiguration
    static class Config {
        @Bean
        public WireMockServer webServer() {
            WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());
            wireMockServer.start();
            return wireMockServer;
        }

        @Bean
        public WebClient.Builder webClient(WireMockServer server) throws SSLException {
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            return WebClient
                    .builder()
                    .clientConnector(new ReactorClientHttpConnector(
                                    HttpClient.create()
                                            .secure(t -> t.sslContext(sslContext))
                                            .resolver(DefaultAddressResolverGroup.INSTANCE)
                            )
                    ).baseUrl(server.baseUrl());
        }

        @Bean
        public HolidayClient client(WebClient.Builder builder) throws SSLException {
            return new HolidayClient(builder);
        }
    }

    @Autowired
    private HolidayClient holidayClient;
    @Autowired
    private WireMockServer server;

    private static String jsonString = "[{\"date\":\"2023-01-01\",\"localName\":\"Nowy Rok\",\"name\":\"New Year's Day\",\"countryCode\":\"PL\"},{\"date\":\"2023-01-06\",\"localName\":\"Święto Trzech Króli\",\"name\":\"Epiphany\",\"countryCode\":\"PL\"},{\"date\":\"2023-04-09\",\"localName\":\"Wielkanoc\",\"name\":\"Easter Sunday\",\"countryCode\":\"PL\"},{\"date\":\"2023-04-10\",\"localName\":\"Drugi Dzień Wielkanocy\",\"name\":\"Easter Monday\",\"countryCode\":\"PL\"},{\"date\":\"2023-05-01\",\"localName\":\"Święto Pracy\",\"name\":\"May Day\",\"countryCode\":\"PL\"},{\"date\":\"2023-05-03\",\"localName\":\"Święto Narodowe Trzeciego Maja\",\"name\":\"Constitution Day\",\"countryCode\":\"PL\"},{\"date\":\"2023-05-28\",\"localName\":\"Zielone Świątki\",\"name\":\"Pentecost\",\"countryCode\":\"PL\"},{\"date\":\"2023-06-08\",\"localName\":\"Boże Ciało\",\"name\":\"Corpus Christi\",\"countryCode\":\"PL\"},{\"date\":\"2023-08-15\",\"localName\":\"Wniebowzięcie Najświętszej Maryi Panny\",\"name\":\"Assumption Day\",\"countryCode\":\"PL\"},{\"date\":\"2023-11-01\",\"localName\":\"Wszystkich Świętych\",\"name\":\"All Saints' Day\",\"countryCode\":\"PL\"},{\"date\":\"2023-11-11\",\"localName\":\"Narodowe Święto Niepodległości\",\"name\":\"Independence Day\",\"countryCode\":\"PL\"},{\"date\":\"2023-12-25\",\"localName\":\"Boże Narodzenie\",\"name\":\"Christmas Day\",\"countryCode\":\"PL\"},{\"date\":\"2023-12-26\",\"localName\":\"Drugi Dzień Bożego Narodzenia\",\"name\":\"St. Stephen's Day\",\"countryCode\":\"PL\"}]";
    private String year = "2022";
    private String countryCode = "PL";

    @Test
    void givenValidRequest_returnListHolidays() throws JsonProcessingException {
        server.stubFor(
                get(urlEqualTo("/holiday"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(jsonString)));

        List<HolidayNagerResponse> holidayNagerResponses =
                holidayClient.findHolidayByYearAndCountry(year, countryCode).block();

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        List<HolidayNagerResponse> expectedHoliday = mapper.readValue(jsonString, new TypeReference<List<HolidayNagerResponse>>(){});

        assertTrue(holidayNagerResponses.size() == expectedHoliday.size());
    }
}
