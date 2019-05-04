package ua.com.epam.swagger;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import ua.com.epam.entity.dto.book.BookWithAuthorAndGenreDto;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .additionalModels(typeResolver.resolve(BookWithAuthorAndGenreDto.class))
                .select()
                .apis(RequestHandlerSelectors.basePackage("ua.com.epam.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .displayOperationId(false)
                .defaultModelsExpandDepth(0)
                .docExpansion(DocExpansion.LIST)
                .operationsSorter(OperationsSorter.ALPHA)
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("REST Library",
                "Simple REST API to reproduce Library",
                "1.0",
                "",
                new Contact("Roman Shmandrovskyi", "https://github.com/RoshS/REST-library", "roman.shmandrovskyj.ki.2014@gmail.com"),
                "",
                "",
                Collections.emptyList());
    }
}
