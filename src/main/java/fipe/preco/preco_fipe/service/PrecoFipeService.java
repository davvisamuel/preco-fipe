package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.response.BrandResponse;
import fipe.preco.preco_fipe.response.FipeInformationResponse;
import fipe.preco.preco_fipe.response.ModelResponse;
import fipe.preco.preco_fipe.response.YearResponse;
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

    public List<YearResponse> findAllYearsByModelId(String vehicleType, String brandId, String modelId) {
        var typeReference = new ParameterizedTypeReference<List<YearResponse>>() {};

        return fipeApiClient.build()
                .get()
                .uri("https://fipe.parallelum.com.br/api/v2/%s/brands/%s/models/%s/years".formatted(vehicleType, brandId, modelId))
                .retrieve()
                .body(typeReference);
    }

    public FipeInformationResponse retrieveFipeInformation(String vehicleType, String brandId, String modelId, String yearId) {

        return fipeApiClient.build()
                .get()
                .uri("https://fipe.parallelum.com.br/api/v2/%s/brands/%s/models/%s/years/%s".formatted(vehicleType, brandId, modelId, yearId))
                .retrieve()
                .body(FipeInformationResponse.class);
    }
}
