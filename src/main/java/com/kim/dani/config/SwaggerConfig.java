package com.kim.dani.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Value("${springdoc.version}")
    String springdocVersion;

    @Bean
    public OpenAPI onOpenAPI(){
        Info info = new Info()
                .title("shop")
                .version(springdocVersion)
                .description("설명");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }

}
