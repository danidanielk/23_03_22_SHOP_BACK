package com.kim.dani;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication()
//exclude = SecurityAutoConfiguration.class
public class DaniApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaniApplication.class, args);
	}

}
