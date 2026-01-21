package fipe.preco.preco_fipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserEmailPutRequest {
    @NotBlank(message = "The field 'newEmail' is required")
    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "The e-mail is not valid")
    @Schema(description = "Email of the user", example = "user@example.com")
    public String newEmail;

    @NotBlank(message = "The field 'currentPassword' is required")
    @Schema(description = "Password of the user", example = "User123@")
    public String currentPassword;
}