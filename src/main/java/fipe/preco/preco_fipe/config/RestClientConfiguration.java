package fipe.preco.preco_fipe.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@AllArgsConstructor
public class RestClientConfiguration {

    private final FipeApiConfiguration fipeApiConfiguration;

    @Bean(name = "fipeApiClient")
    public RestClient.Builder fipeApiClient() {
        return RestClient
                .builder()
                .baseUrl(fipeApiConfiguration.baseUrl());
    }
}
