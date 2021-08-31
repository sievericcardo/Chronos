package it.kernelpanic.chronos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main application file for the Chronos Backend
 * It uses SpringBoot to create a daemon that will handle all requests done to the backend
 */
@SpringBootApplication
public class ChronosApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ChronosApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ChronosApplication.class, args);
    }

}

/*
 * Codename: Scarlett
 */