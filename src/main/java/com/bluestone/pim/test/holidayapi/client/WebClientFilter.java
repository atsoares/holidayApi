package com.bluestone.pim.test.holidayapi.client;

import com.bluestone.pim.test.holidayapi.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

/**
 * The class WebClientFilter
 */
@Slf4j
public class WebClientFilter {

    /**
     * Method logRequest
     */
    public static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            logMethodAndUrl(request);
            logHeaders(request);

            return Mono.just(request);
        });
    }

    /**
     * Method logResponse
     */
    public static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            logStatus(response);
            logHeaders(response);

            return Mono.just(response);
        });
    }

    /**
     * Method logStatus
     */
    private static void logStatus(ClientResponse response) {
        HttpStatus status = response.statusCode();
        log.debug("Returned status code {} ({})", status.value(), status.getReasonPhrase());
    }

    /**
     * Method handleError
     */
    public static ExchangeFilterFunction handleError() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            response.statusCode();
            if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
                return response.bodyToMono(String.class)
                        .defaultIfEmpty(response.statusCode().getReasonPhrase())
                        .flatMap(body -> {
                            log.debug("Body is {}", body);
                            return Mono.error(new ResponseException(body, response.rawStatusCode()));
                        });
            } else {
                return Mono.just(response);
            }
        });
    }

    /**
     * Method logHeaders
     *
     * @param response ClientResponse response
     */
    private static void logHeaders(ClientResponse response) {
        response.headers().asHttpHeaders().forEach((name, values) -> {
            values.forEach(value -> {
                logNameAndValuePair(name, value);
            });
        });
    }

    /**
     * Method logHeaders
     *
     * @param request ClientRequest request
     */
    private static void logHeaders(ClientRequest request) {
        request.headers().forEach((name, values) -> {
            values.forEach(value -> {
                logNameAndValuePair(name, value);
            });
        });
    }

    /**
     * Method logNameAndValuePair
     *
     * @param name String name
     * @param value String value
     */
    private static void logNameAndValuePair(String name, String value) {
        log.debug("{}={}", name, value);
    }

    /**
     * Method logMethodAndUrl
     *
     * @param request ClientRequest request
     */
    private static void logMethodAndUrl(ClientRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.method().name());
        sb.append(" to ");
        sb.append(request.url());

        log.debug(sb.toString());
    }
}
