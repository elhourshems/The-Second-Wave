package com.thesecondwave.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;


@OpenAPIDefinition(
    info = @io.swagger.v3.oas.annotations.info.Info(
        title = "The Second Wave Store API",
        version = "1.0",
        description = "API for managing products, users, carts, and orders in The Second Wave Store."
    )
)
@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }
}
