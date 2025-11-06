package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.response.BrandResponse;
import fipe.preco.preco_fipe.dto.response.FipeInformationResponse;
import fipe.preco.preco_fipe.dto.response.ModelResponse;
import fipe.preco.preco_fipe.dto.response.YearResponse;
import fipe.preco.preco_fipe.exception.DefaultErrorMessage;
import fipe.preco.preco_fipe.service.FipeApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api")
@RequiredArgsConstructor
@Tag(name = "Fipe API", description = "Endpoints for consulting vehicle information from the FIPE table")
public class FipeApiController {

    private final FipeApiService fipeApiService;

    @GetMapping("/{vehicleType}")
    @Operation(summary = "Get all brands by vehicle type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of brands retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BrandResponse.class))),
    })
    public ResponseEntity<List<BrandResponse>> findAllBrandsByType(@PathVariable String vehicleType) {
        var allBrandsByType = fipeApiService.findAllBrandsByType(vehicleType);
        return ResponseEntity.ok(allBrandsByType);
    }

    @GetMapping("/{vehicleType}/brands/{brandId}/models")
    @Operation(summary = "Get all models by brand ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of models retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ModelResponse.class))),
    })
    public ResponseEntity<List<ModelResponse>> findAllModelsByBrandId(@PathVariable String vehicleType, @PathVariable String brandId) {
        var allModelsByBrandId = fipeApiService.findAllModelsByBrandId(vehicleType, brandId);
        return ResponseEntity.ok(allModelsByBrandId);
    }

    @GetMapping("/{vehicleType}/brands/{brandId}/models/{modelId}/years")
    @Operation(summary = "Get all years by model ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of years retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BrandResponse.class))),
    })
    public ResponseEntity<List<YearResponse>> findAllYearsByModelId(@PathVariable String vehicleType,
                                                                    @PathVariable String brandId,
                                                                    @PathVariable String modelId) {
        var allModelsByBrandId = fipeApiService.findAllYearsByModelId(vehicleType, brandId, modelId);
        return ResponseEntity.ok(allModelsByBrandId);
    }

    @GetMapping("/{vehicleType}/brands/{brandId}/models/{modelId}/years/{yearId}")
    @Operation(summary = "Get FIPE information by year ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fipe information retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FipeInformationResponse.class))),

            @ApiResponse(responseCode = "404", description = "Comparison not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorMessage.class))),

            @ApiResponse(responseCode = "409", description = "Comparison limit exceeded",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorMessage.class)))
    })
    public ResponseEntity<FipeInformationResponse> retrieveFipeInformation(@AuthenticationPrincipal User user,
                                                                           @PathVariable String vehicleType,
                                                                           @PathVariable String brandId,
                                                                           @PathVariable String modelId,
                                                                           @PathVariable String yearId,
                                                                           @RequestParam(required = false) Integer comparisonId) {
        var allModelsByBrandId = fipeApiService.retrieveFipeInformation(user, comparisonId, vehicleType, brandId, modelId, yearId);
        return ResponseEntity.ok(allModelsByBrandId);
    }
}
