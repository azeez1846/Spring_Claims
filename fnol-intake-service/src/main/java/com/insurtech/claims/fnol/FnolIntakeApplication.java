package com.insurtech.claims.fnol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.insurtech.claims")
public class FnolIntakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FnolIntakeApplication.class, args);
    }
}
