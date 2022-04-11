package com.bluebug.gamehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class GameHubAuthApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameHubAuthApiApplication.class, args);
    }
}
