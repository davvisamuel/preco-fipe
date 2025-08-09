package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.response.BrandResponse;
import fipe.preco.preco_fipe.response.ModelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrecoFipeService {

    public final RestClient.Builder fipeApiClient;

    public List<BrandResponse> findAllBrandsByType(String vehicleType) {
        var typeReference = new ParameterizedTypeReference<List<BrandResponse>>() {};

        return fipeApiClient.build()
                .get()
                .uri("https://fipe.parallelum.com.br/api/v2/%s/brands".formatted(vehicleType))
                .retrieve()
                .body(typeReference);
    }

    public List<ModelResponse> findAllModelsByBrandId(String vehicleType, String brandId) {
        var typeReference = new ParameterizedTypeReference<List<ModelResponse>>() {};

        return fipeApiClient.build()
                .get()
                .uri("https://fipe.parallelum.com.br/api/v2/%s/brands/%s/models".formatted(vehicleType, brandId))
                .retrieve()
                .body(typeReference);
    }
}
