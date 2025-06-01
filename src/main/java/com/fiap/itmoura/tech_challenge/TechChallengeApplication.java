package com.fiap.itmoura.tech_challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TechChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechChallengeApplication.class, args);
	}

}
