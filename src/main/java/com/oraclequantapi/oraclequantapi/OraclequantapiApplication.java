package com.oraclequantapi.oraclequantapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

 // Application entry point.
 // components under this package

@SpringBootApplication
public class OraclequantapiApplication {

	public static void main(String[] args) {

		// Bootstraps Spring context then listens on server port
		SpringApplication.run(OraclequantapiApplication.class, args);
	}

}
