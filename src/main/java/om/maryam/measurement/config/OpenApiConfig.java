package om.maryam.measurement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI metadata for the API.  Kept intentionally minimal so the
 * generated documentation focuses on the endpoints, not on branding.
 *
 *  - Swagger UI : /maryam/swagger-ui/index.html
 *  - JSON spec  : /maryam/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI maryamOpenApi() {
        return new OpenAPI().info(new Info()
                .title("MARYAM Measurement Conversion API")
                .description("Decodes encoded measurement strings into numeric package totals.")
                .version("1.0.0"));
    }
}
