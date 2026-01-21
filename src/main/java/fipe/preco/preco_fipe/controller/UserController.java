package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.request.UserEmailPutRequest;
import fipe.preco.preco_fipe.dto.request.UserPasswordPutRequest;
import fipe.preco.preco_fipe.dto.request.UserPostRequest;
import fipe.preco.preco_fipe.dto.request.UserPutRequest;
import fipe.preco.preco_fipe.dto.response.UserGetResponse;
import fipe.preco.preco_fipe.dto.response.UserPostResponse;
import fipe.preco.preco_fipe.exception.ApiError;
import fipe.preco.preco_fipe.mapper.UserMapper;
import fipe.preco.preco_fipe.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@AllArgsConstructor
@Log4j2
@Tag(name = "User", description = "Manage User's")
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    @Operation(summary = "Get all users (paginated)",
            description = "Admin only.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of User retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),

            @ApiResponse(responseCode = "403", description = "Forbidden - only administrators can access this resource"),
    })
    public ResponseEntity<Page<UserGetResponse>> findAll(@ParameterObject Pageable pageable) {
        var userPage = service.findAll(pageable);

        Page<UserGetResponse> userGetResponsePage = userPage.map(mapper::toUserGetResponse);

        return ResponseEntity.ok(userGetResponsePage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID",
            description = "Admin only.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserGetResponse.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),

            @ApiResponse(responseCode = "403", description = "Forbidden - only administrators can access this resource"),
    })
    public ResponseEntity<UserGetResponse> findById(@PathVariable Integer id) {
        var user = service.findById(id);

        var userGetResponse = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping("/register")
    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPostResponse.class))),

            @ApiResponse(responseCode = "400", description = "Email already exists or invalid request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
    })
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest userPostRequest) {
        log.debug("Request received for '{}'", userPostRequest);

        var user = mapper.toUser(userPostRequest);

        var savedUser = service.save(user);

        var userPostResponse = mapper.toUserPostResponse(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }


    @PutMapping
    @Operation(summary = "Update a user", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User updated successfully"),

            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),

            @ApiResponse(responseCode = "400", description = "Email already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),

            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have permission or token missing")

    })
    public ResponseEntity<Void> update(@AuthenticationPrincipal User user, @RequestBody @Valid UserPutRequest userPutRequest) {
        var userToUpdate = mapper.toUser(userPutRequest);

        service.update(user, userToUpdate);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/email")
    public ResponseEntity<Void> updateEmail(@AuthenticationPrincipal User user, @RequestBody @Valid UserEmailPutRequest userEmailPutRequest) {
        service.updateEmail(user, userEmailPutRequest);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal User user, @RequestBody @Valid UserPasswordPutRequest userPasswordPutRequest) {
        service.updatePassword(user, userPasswordPutRequest);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping
    @Operation(summary = "Delete a user", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),

            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),

            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have permission or token missing")
    })
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user) {
        service.delete(user);
        return ResponseEntity.noContent().build();
    }
}
