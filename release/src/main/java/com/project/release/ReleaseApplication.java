package com.project.release;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ReleaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReleaseApplication.class, args);
	}

}
