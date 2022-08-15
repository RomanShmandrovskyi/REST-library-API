package ua.com.api.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "ua.com.api")
@EnableJpaRepositories("ua.com.api.repository")
@EntityScan("ua.com.api.entity")
@SpringBootApplication
@OpenAPIDefinition
public class App {
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        SpringApplication.run(App.class, args);
    }
}