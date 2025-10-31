package fipe.preco.preco_fipe.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPostRequest {

    @NotBlank(message = "The field 'email' is required")
    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "The e-mail is not valid")
    public String email;

    @NotBlank(message = "The field 'password' is required")
    public String password;
}