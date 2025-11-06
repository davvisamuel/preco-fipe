package fipe.preco.preco_fipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPostRequest {
    @NotBlank(message = "The field 'email' is required")
    @Schema(description = "Email of the user", example = "user@example.com")
    private String email;
    @NotBlank(message = "The field 'password' is required")
    @Schema(description = "Password of the user", example = "User123@")
    private String password;
}
