package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.response.BrandResponse;
import fipe.preco.preco_fipe.service.PrecoFipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/precoFipe")
@RequiredArgsConstructor
public class PrecoFipeController {

    private final PrecoFipeService precoFipeService;

    @GetMapping
    public ResponseEntity<List<BrandResponse>> findAllBrandsByType(@RequestParam String vehicleType) {
        var allBrandsByType = precoFipeService.findAllBrandsByType(vehicleType);
        return ResponseEntity.ok(allBrandsByType);
    }
}
