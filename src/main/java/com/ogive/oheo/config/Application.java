package com.ogive.oheo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.ogive.oheo.persistence.repo")
@EntityScan("com.ogive.oheo.persistence.model")
@SpringBootApplication(scanBasePackages = {"com.ogive.oheo.controller"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
