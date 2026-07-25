package com.insurtech.claims.adjuster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.insurtech.claims.adjuster.repository")
public class AdjusterAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdjusterAssignmentApplication.class, args);
    }
}
