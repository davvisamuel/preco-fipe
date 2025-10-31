package fipe.preco.preco_fipe.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPostRequest {
    private String email;
    private String password;
}
