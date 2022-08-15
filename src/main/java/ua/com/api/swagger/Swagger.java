package ua.com.api.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info().title("REST Library API")
                        .description("Simple REST API to reproduce Library")
                        .version("1.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("See on GitHub")
                        .url("https://github.com/RomanShmandrovskyi/REST-library-API"));
    }
}