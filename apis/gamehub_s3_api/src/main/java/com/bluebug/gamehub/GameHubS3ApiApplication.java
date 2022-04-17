package com.bluebug.gamehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class GameHubS3ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameHubS3ApiApplication.class,args);
    }
}
