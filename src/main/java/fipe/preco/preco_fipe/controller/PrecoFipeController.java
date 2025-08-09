package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.response.BrandResponse;
import fipe.preco.preco_fipe.response.FipeInformationResponse;
import fipe.preco.preco_fipe.response.ModelResponse;
import fipe.preco.preco_fipe.response.YearResponse;
import fipe.preco.preco_fipe.service.PrecoFipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/api")
@RequiredArgsConstructor
public class PrecoFipeController {

    private final PrecoFipeService precoFipeService;

    @GetMapping("/{vehicleType}")
    public ResponseEntity<List<BrandResponse>> findAllBrandsByType(@PathVariable String vehicleType) {
        var allBrandsByType = precoFipeService.findAllBrandsByType(vehicleType);
        return ResponseEntity.ok(allBrandsByType);
    }

    @GetMapping("/{vehicleType}/brands/{brandId}/models")
    public ResponseEntity<List<ModelResponse>> findAllModelsByBrandId(@PathVariable String vehicleType, @PathVariable String brandId) {
        var allModelsByBrandId = precoFipeService.findAllModelsByBrandId(vehicleType, brandId);
        return ResponseEntity.ok(allModelsByBrandId);
    }

    @GetMapping("/{vehicleType}/brands/{brandId}/models/{modelId}/years")
    public ResponseEntity<List<YearResponse>> findAllYearsByModelId(@PathVariable String vehicleType,
                                                                    @PathVariable String brandId,
                                                                    @PathVariable String modelId) {
        var allModelsByBrandId = precoFipeService.findAllYearsByModelId(vehicleType, brandId, modelId);
        return ResponseEntity.ok(allModelsByBrandId);
    }

    @GetMapping("/{vehicleType}/brands/{brandId}/models/{modelId}/years/{yearId}")
    public ResponseEntity<FipeInformationResponse> retrieveFipeInformation(@PathVariable String vehicleType,
                                                                           @PathVariable String brandId,
                                                                           @PathVariable String modelId,
                                                                           @PathVariable String yearId) {
        var allModelsByBrandId = precoFipeService.retrieveFipeInformation(vehicleType, brandId, modelId, yearId);
        return ResponseEntity.ok(allModelsByBrandId);
    }
}
