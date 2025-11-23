package com.cjkim.kimjuim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.cjkim.kimjuim")
public class KimjuimApplication {

	public static void main(String[] args) {
		SpringApplication.run(KimjuimApplication.class, args);
	}

}
