package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.response.ConsultationGetResponse;
import fipe.preco.preco_fipe.exception.DefaultErrorMessage;
import fipe.preco.preco_fipe.mapper.ConsultationMapper;
import fipe.preco.preco_fipe.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/consultation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Consultations", description = "Manage user's consultations")
@SecurityRequirement(name = "bearerAuth")
public class ConsultationController {
    private final ConsultationService consultationService;
    private final ConsultationMapper consultationMapper;

    @GetMapping
    @Operation(summary = "Get all consultations (paginated)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of consultation retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),

            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have permission or token missing")
    })
    public ResponseEntity<Page<ConsultationGetResponse>> findAllPaginated(@ParameterObject Pageable pageable) {
        log.debug("Request received for '{}'", pageable);

        var consultationPage = consultationService.findAllPaginated(pageable);

        var consultationGetResponsePage = consultationPage.map(consultationMapper::toConsultationGetResponse);

        return ResponseEntity.ok(consultationGetResponsePage);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a consultation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comparison deleted successfully"),

            @ApiResponse(responseCode = "404", description = "Consultation not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorMessage.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),

            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have permission or token missing")
    })
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        log.debug("Request received for delete consultation '{}'", id);

        consultationService.delete(id, user);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all consultations for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comparisons deleted successfully"),

            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),

            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have permission or token missing")
    })
    public ResponseEntity<Void> deleteAllByUser(@AuthenticationPrincipal User user) {
        log.debug("Request received for delete all consultations of '{}'", user);

        consultationService.deleteAllByUser(user);

        return ResponseEntity.noContent().build();
    }

}
