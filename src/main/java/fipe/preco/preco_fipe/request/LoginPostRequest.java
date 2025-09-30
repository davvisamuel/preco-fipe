package fipe.preco.preco_fipe.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPostRequest {
    private String email;
    private String password;
}
