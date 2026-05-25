package com.oraclequantapi.oraclequantapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
(properties = {
		"spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1",
		"spring.datasource.driver-class-name=oracle.jdbc.OracleDriver",
		"spring.datasource.username=system",
		"spring.datasource.password=29999login",
		"spring.jpa.database-platform=org.hibernate.dialect.OracleDialect"
})
class OraclequantapiApplicationTests {

	@Test
	void contextLoads() {
	}

}

