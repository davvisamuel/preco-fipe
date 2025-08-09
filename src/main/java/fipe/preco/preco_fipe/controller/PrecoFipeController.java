package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.response.BrandResponse;
import fipe.preco.preco_fipe.response.ModelResponse;
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
}
