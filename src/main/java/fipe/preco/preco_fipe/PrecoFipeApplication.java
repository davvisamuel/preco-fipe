package fipe.preco.preco_fipe;

import fipe.preco.preco_fipe.config.FipeApiConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = FipeApiConfiguration.class)
public class PrecoFipeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrecoFipeApplication.class, args);
    }

}
