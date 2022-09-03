package ua.com.api.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {

    @Bean
    public OpenAPI api() {
        String schemeName = "Library security";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(new Components()
                        .addSecuritySchemes(schemeName, new SecurityScheme()
                                .name(schemeName)
                                .scheme("basic")
                                .type(SecurityScheme.Type.HTTP)
                                .in(SecurityScheme.In.HEADER)))
                .info(new Info().title("REST Library API")
                        .description("Simple REST API to reproduce Library")
                        .version("1.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("See on GitHub")
                        .url("https://github.com/RomanShmandrovskyi/REST-library-API"));
    }
}