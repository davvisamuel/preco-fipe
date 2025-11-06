package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.dto.response.FipeInformationResponse;

public class FipeInformationUtils {

    public static FipeInformationResponse newFipeInformationResponse() {

        return FipeInformationResponse.builder()
                .vehicleType(1)
                .price("R$ 10.980,00")
                .brand("Acura")
                .model("Integra GS 1.8")
                .modelYear("1992")
                .fuel("Gasolina")
                .codeFipe("038003-2")
                .referenceMonth("novembro de 2025")
                .fuelAcronym("G")
                .build();
    }
}
