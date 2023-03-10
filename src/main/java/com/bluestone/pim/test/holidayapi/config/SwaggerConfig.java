package com.bluestone.pim.test.holidayapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig class
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("localhost:8080")
    private String baseUrl;

    @Value("Holiday API for TechInterview - Bluestone PIM")
    private String apiTitle;

    @Value("Holiday API")
    private String apiInfo;

    @Bean
    public Docket microServicesApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(baseUrl)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(metaData(apiInfo));
    }

    private ApiInfo metaData(String description) {
        final String projectVersion = "1.0";
        return new ApiInfoBuilder()
                .description(description)
                .version(projectVersion)
                .title(apiTitle)
                .contact(
                        new Contact("techinterview-ia holidayApi", "https://github.com/atsoares", "aleexsoarees@gmail.com"))
                .license("Holiday Api Test © 2023")
                .build();
    }

}
