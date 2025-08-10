package fipe.preco.preco_fipe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fipe-api")
public record FipeApiConfiguration(String baseUrl, String brandsUri, String modelsUri, String yearsUri,
                                   String fipeInformationUri) {
}
