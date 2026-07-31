package com.example.afrorealityapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class AfroRealityApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AfroRealityApiApplication.class, args);
    }

}
