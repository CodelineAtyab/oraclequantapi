package com.oraclequantapi.oraclequantapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.oraclequantapi.oraclequantapi")
public class OraclequantapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OraclequantapiApplication.class, args);
	}

}
