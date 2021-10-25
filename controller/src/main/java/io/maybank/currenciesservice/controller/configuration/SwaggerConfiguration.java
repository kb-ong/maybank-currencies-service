package io.maybank.currenciesservice.controller.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openApi(@Value("${springdoc.openapi.version}") String apiVersion) {

        return new OpenAPI()
                .info(new Info()
                        .title("Maybank: Gateway/Currencies Service API")
                        .version(apiVersion)
                        .description("Documentation of the Maybank: Gateway/Currencies Service API"));
    }
}