package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.request.FavoritePostRequest;
import fipe.preco.preco_fipe.dto.response.FavoriteGetResponse;
import fipe.preco.preco_fipe.dto.response.FavoritePostResponse;
import fipe.preco.preco_fipe.exception.ApiError;
import fipe.preco.preco_fipe.exception.DefaultErrorMessage;
import fipe.preco.preco_fipe.mapper.FavoriteMapper;
import fipe.preco.preco_fipe.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/v1/favorite")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Favorites", description = "Manage user's favorite vehicles")
@SecurityRequirement(name = "bearerAuth")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final FavoriteMapper favoriteMapper;

    @PostMapping
    @Operation(summary = "Create a favorite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Favorite create successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoritePostResponse.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),

            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have permission or token missing")
    })
    public ResponseEntity<FavoritePostResponse> save(@AuthenticationPrincipal User user, @RequestBody @Valid FavoritePostRequest favoritePostRequest) {
        log.debug("Request received for '{}'", favoritePostRequest);

        var codeFipe = favoritePostRequest.getCodeFipe();

        var modelYear = favoritePostRequest.getCodeFipe();

        var fuelAcronym = favoritePostRequest.getCodeFipe();

        var favorite = favoriteService.save(user, codeFipe, modelYear, fuelAcronym);

        var favoritePostResponse = favoriteMapper.toFavoritePostResponse(favorite);

        return ResponseEntity.status(HttpStatus.CREATED).body(favoritePostResponse);
    }

    @GetMapping("/paginated")
    @Operation(summary = "List all favorites paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of favorites retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),

            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have permission or token missing")
    })
    public ResponseEntity<Page<FavoriteGetResponse>> findAllPaginated(@AuthenticationPrincipal User user, @ParameterObject Pageable pageable) {
        log.debug("Request received to list all favorites paginated");

        var favoriteGetResponsePage = favoriteService.findAllPaginated(user, pageable).map(favoriteMapper::toFavoriteGetResponse);

        return ResponseEntity.ok(favoriteGetResponsePage);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a favorite by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Favorite deleted successfully"),

            @ApiResponse(responseCode = "404", description = "Favorite not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DefaultErrorMessage.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),

            @ApiResponse(responseCode = "403", description = "Forbidden - user not allowed to delete this favorite"),
    })
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        log.debug("request received for delete favorite");

        favoriteService.delete(user, id);

        return ResponseEntity.noContent().build();
    }
}
