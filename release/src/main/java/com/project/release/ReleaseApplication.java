package com.project.release;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReleaseApplication {

	private static final String PROPERTIES =
			"spring.config.location="
					+"classpath:/application.yml"
					+",classpath:/security.yml";

	public static void main(String[] args) {
		SpringApplication.run(ReleaseApplication.class, args);
	}

}
