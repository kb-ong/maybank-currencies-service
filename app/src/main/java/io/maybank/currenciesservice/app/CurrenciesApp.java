package io.maybank.currenciesservice.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@EnableJpaRepositories(basePackages = "io.maybank.currenciesservice.persistence")
@SpringBootApplication(scanBasePackages = {"io.maybank.currenciesservice"})
public class CurrenciesApp {
    public static void main(String[] args) {
        SpringApplication.run(CurrenciesApp.class, args);
    }
}
