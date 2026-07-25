package com.insurtech.claims.reserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.insurtech.claims.reserve.repository")
public class ReserveSettlementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReserveSettlementApplication.class, args);
    }
}
