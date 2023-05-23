package com.api.parkingcontrol.configs;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import static springfox.documentation.builders.PathSelectors.regex;


@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig  {


    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.api.parkingcontrol"))
                .paths(regex("/parking-spot"))
                .build()
                .apiInfo(this.metaApiInfo());
    }

    private ApiInfo metaApiInfo(){

        ApiInfo apiInfo = new ApiInfo(
                "API de Estacionamento",
                "API REST de estacionamento",
                "1.0",
                "Term Service",
                new Contact("Alex Santos","https://www.youtube.com/watch?v=qtHUwMqOYC0",
                        "alex.ssnt@gmail.com"),
                "",
                "",null
        );
       return apiInfo;
    }
}
