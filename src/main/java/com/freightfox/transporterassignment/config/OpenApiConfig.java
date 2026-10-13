package com.freightfox.transporterassignment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FreightFox - Transporter Assignment on Lanes API")
                        .version("1.0.0")
                        .description("REST API service to optimize transporter procurement allocations across Indian freight lanes with minimum cost and full coverage constraints.")
                        .contact(new Contact()
                                .name("Harsh Bohra")
                                .email("harshbohra@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}

