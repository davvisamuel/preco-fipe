package fipe.preco.preco_fipe.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginPostResponse {
    private String token;
}
