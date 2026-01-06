package fipe.preco.preco_fipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoritePostRequest {

    @NotBlank(message = "The field 'codeFipe' is required")
    @Schema(description = "This is the vehicle’s FIPE code", example = "005456-9")
    private String codeFipe;

    @NotBlank(message = "The field 'modelYear' is required")
    @Schema(description = "This is the model year", example = "2017")
    private String modelYear;

    @NotBlank(message = "The field 'fuelAcronym' is required")
    @Schema(description = "This is the fuel acronym", example = "G")
    private String fuelAcronym;
}
