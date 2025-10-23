package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.response.BrandResponse;
import fipe.preco.preco_fipe.response.FipeInformationResponse;
import fipe.preco.preco_fipe.response.ModelResponse;
import fipe.preco.preco_fipe.response.YearResponse;
import fipe.preco.preco_fipe.service.FipeApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api")
@RequiredArgsConstructor
public class FipeApiController {

    private final FipeApiService fipeApiService;

    @GetMapping("/{vehicleType}")
    public ResponseEntity<List<BrandResponse>> findAllBrandsByType(@PathVariable String vehicleType) {
        var allBrandsByType = fipeApiService.findAllBrandsByType(vehicleType);
        return ResponseEntity.ok(allBrandsByType);
    }

    @GetMapping("/{vehicleType}/brands/{brandId}/models")
    public ResponseEntity<List<ModelResponse>> findAllModelsByBrandId(@PathVariable String vehicleType, @PathVariable String brandId) {
        var allModelsByBrandId = fipeApiService.findAllModelsByBrandId(vehicleType, brandId);
        return ResponseEntity.ok(allModelsByBrandId);
    }

    @GetMapping("/{vehicleType}/brands/{brandId}/models/{modelId}/years")
    public ResponseEntity<List<YearResponse>> findAllYearsByModelId(@PathVariable String vehicleType,
                                                                    @PathVariable String brandId,
                                                                    @PathVariable String modelId) {
        var allModelsByBrandId = fipeApiService.findAllYearsByModelId(vehicleType, brandId, modelId);
        return ResponseEntity.ok(allModelsByBrandId);
    }

    @GetMapping("/{vehicleType}/brands/{brandId}/models/{modelId}/years/{yearId}")
    public ResponseEntity<FipeInformationResponse> retrieveFipeInformation(@AuthenticationPrincipal User user,
                                                                           @PathVariable String vehicleType,
                                                                           @PathVariable String brandId,
                                                                           @PathVariable String modelId,
                                                                           @PathVariable String yearId,
                                                                           @RequestParam Integer comparisonId) {
        var allModelsByBrandId = fipeApiService.retrieveFipeInformation(user, comparisonId, vehicleType, brandId, modelId, yearId);
        return ResponseEntity.ok(allModelsByBrandId);
    }
}
