package com.oraclequantapi.oraclequantapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class OraclequantapiApplication {

	public static void main(String[] args) {
		log.info("Starting PKC API...");
		SpringApplication.run(OraclequantapiApplication.class, args);
		log.info("PKC API started successfully");
	}

}
