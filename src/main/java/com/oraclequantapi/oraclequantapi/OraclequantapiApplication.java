package com.oraclequantapi.oraclequantapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
/**
 * Main starting point of the API.
 *
 * Spring Boot uses this class to start the web server, load the settings from
 * application.properties, and create the controllers, services, and repositories.
 */
public class OraclequantapiApplication {

	public static void main(String[] args) {
		// These log lines make it easy to see in the console/file when the app starts.
		log.info("Starting PKC API...");
		SpringApplication.run(OraclequantapiApplication.class, args);
		log.info("PKC API started successfully");
	}

}
