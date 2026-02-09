package com.soundcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SoundCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoundCloudApplication.class, args);
    }
}
