package fipe.preco.preco_fipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserPasswordPutRequest {
    @NotBlank(message = "The field 'currentPassword' is required")
    @Schema(description = "Password of the user", example = "User123@")
    public String currentPassword;

    @NotBlank(message = "The field 'newPassword' is required")
    @Schema(description = "Password of the user", example = "User123@")
    public String newPassword;
}