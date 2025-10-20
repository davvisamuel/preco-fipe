package fipe.preco.preco_fipe.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoritePostRequest {

    @NotBlank(message = "The field 'codeFipe' is required")
    private String codeFipe;
}
