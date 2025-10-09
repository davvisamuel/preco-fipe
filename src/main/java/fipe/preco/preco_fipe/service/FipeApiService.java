package fipe.preco.preco_fipe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fipe.preco.preco_fipe.config.FipeApiConfiguration;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FipeApiService {
    private final ObjectMapper mapper;
    public final RestClient.Builder fipeApiClient;
    public final FipeApiConfiguration fipeApiConfiguration;
    public final HistoryService historyService;

    public List<BrandResponse> findAllBrandsByType(String vehicleType) {
        var typeReference = new ParameterizedTypeReference<List<BrandResponse>>() {};

        return fipeApiClient.build()
                .get()
                .uri(fipeApiConfiguration.baseUrl() + fipeApiConfiguration.brandsUri(), vehicleType)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    var body = new String(response.getBody().readAllBytes());
                    var fipeErrorResponse = mapper.readValue(body, FipeErrorResponse.class);
                    throw new NotFoundException(fipeErrorResponse.toString());
                }))
                .body(typeReference);
    }

    public List<ModelResponse> findAllModelsByBrandId(String vehicleType, String brandId) {
        var typeReference = new ParameterizedTypeReference<List<ModelResponse>>() {};

        return fipeApiClient.build()
                .get()
                .uri(fipeApiConfiguration.baseUrl() + fipeApiConfiguration.modelsUri(), vehicleType, brandId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    var body = new String(response.getBody().readAllBytes());
                    var fipeErrorResponse = mapper.readValue(body, FipeErrorResponse.class);
                    throw new NotFoundException(fipeErrorResponse.toString());
                }))
                .body(typeReference);
    }

    public List<YearResponse> findAllYearsByModelId(String vehicleType, String brandId, String modelId) {
        var typeReference = new ParameterizedTypeReference<List<YearResponse>>() {};

        return fipeApiClient.build()
                .get()
                .uri(fipeApiConfiguration.baseUrl() + fipeApiConfiguration.yearsUri(), vehicleType, brandId, modelId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    var body = new String(response.getBody().readAllBytes());
                    var fipeErrorResponse = mapper.readValue(body, FipeErrorResponse.class);
                    throw new NotFoundException(fipeErrorResponse.toString());
                }))
                .body(typeReference);
    }

    public FipeInformationResponse retrieveFipeInformation(String vehicleType, String brandId, String modelId, String yearId) {

        var fipeInformationResponse = fipeApiClient.build()
                .get()
                .uri(fipeApiConfiguration.baseUrl() + fipeApiConfiguration.fipeInformationUri(), vehicleType, brandId, modelId, yearId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    var body = new String(response.getBody().readAllBytes());
                    var fipeErrorResponse = mapper.readValue(body, FipeErrorResponse.class);
                    throw new NotFoundException(fipeErrorResponse.toString());
                }))
                .body(FipeInformationResponse.class);

        saveIfAuthenticated(fipeInformationResponse);

        return fipeInformationResponse;
    }

    public void saveIfAuthenticated(FipeInformationResponse fipeInformationResponse) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("USER"))) {
            return;
        }

        historyService.saveConsultation(authentication, fipeInformationResponse);
    }
}
