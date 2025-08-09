package fipe.preco.preco_fipe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean(name = "fipeApiClient")
    public RestClient.Builder fipeApiClient() {
        return RestClient
                .builder()
                .baseUrl("https://fipe.parallelum.com.br/api");
    }
}
