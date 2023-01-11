package com.snowballer.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SnowballerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnowballerApplication.class, args);
	}

}
