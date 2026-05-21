package com.oraclequantapi.oraclequantapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (
		exclude = {
				org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
		}
)
public class OraclequantapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OraclequantapiApplication.class, args);
	}

}
