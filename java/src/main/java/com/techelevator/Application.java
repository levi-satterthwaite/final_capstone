package com.techelevator;

import com.techelevator.storage.StorageProperties;
import com.techelevator.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class) //telling the application we are using these properties
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Initialing services
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            // Don't want uploads getting deleted
            // storageService.deleteAll();
            storageService.init();
        };
    }

}
