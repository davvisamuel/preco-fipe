package fipe.preco.preco_fipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPutRequest {
    @NotBlank(message = "The field 'email' is required")
    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "The e-mail is not valid")
    @Schema(description = "Email of the user", example = "user@example.com")
    public String email;

    @NotNull(message = "The field 'password' is required")
    @Schema(description = "Password of the user", example = "User123@")
    public String password;
}