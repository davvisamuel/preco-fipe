package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.response.ComparisonGetResponse;
import fipe.preco.preco_fipe.dto.response.ComparisonPostResponse;
import fipe.preco.preco_fipe.exception.ApiError;
import fipe.preco.preco_fipe.exception.DefaultErrorMessage;
import fipe.preco.preco_fipe.mapper.ComparisonMapper;
import fipe.preco.preco_fipe.service.ComparisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comparison")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Comparisons", description = "Manage user's comparison")
public class ComparisonController {

    private final ComparisonService comparisonService;
    private final ComparisonMapper comparisonMapper;

    @PostMapping
    @Operation(summary = "Create a comparison")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comparison create successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ComparisonPostResponse.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    private ResponseEntity<ComparisonPostResponse> save(@AuthenticationPrincipal User user) {
        log.debug("Request received for creates comparison");

        var comparisonToSave = comparisonMapper.toComparison(user);

        var savedComparison = comparisonService.save(comparisonToSave);

        var comparisonPostResponse = comparisonMapper.toComparisonPostResponse(savedComparison);

        return ResponseEntity.status(HttpStatus.CREATED).body(comparisonPostResponse);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a comparison by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comparison deleted successfully"),

            @ApiResponse(responseCode = "404", description = "Comparison not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorMessage.class)))
    })
    private ResponseEntity<Void> delete(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        log.debug("Request received for delete comparison where id '{}'", id);

        comparisonService.delete(id, user);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all comparisons (paginated)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of comparison retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ComparisonPostResponse.class))),
    })
    private ResponseEntity<Page<ComparisonGetResponse>> findAllPaginated(@AuthenticationPrincipal User user, @ParameterObject Pageable pageable) {
        log.debug("Request received for '{}'", pageable);

        var comparisonPage = comparisonService.findAllPaginated(user, pageable);

        var comparisonGetResponsePage = comparisonPage.map(comparisonMapper::toComparisonGetResponse);

        return ResponseEntity.ok(comparisonGetResponsePage);
    }
}
