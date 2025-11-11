package fipe.preco.preco_fipe;

import fipe.preco.preco_fipe.config.FipeApiConfiguration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;

@OpenAPIDefinition(
        info = @Info(
                title = "Preço Fipe API",
                version = "1.0",
                description = "API of the Preço FIPE system for retrieving vehicle data, performing comparisons, managing favorites, and storing consultation history."
        ))

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)

@EnableRabbit
@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = FipeApiConfiguration.class)
@EnableCaching
public class PrecoFipeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrecoFipeApplication.class, args);
    }

}
