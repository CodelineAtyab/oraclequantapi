package om.maryam.measurement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the MARYAM Measurement Conversion API.
 *
 * Bootstraps Spring Boot auto-configuration, component scanning,
 * embedded Tomcat, JPA and Swagger.
 */
@SpringBootApplication
public class MaryamMeasurementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaryamMeasurementApplication.class, args);
    }
}
