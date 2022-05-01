package com.bluebug.gamehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GameHubPostApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameHubPostApiApplication.class,args);
    }
}
