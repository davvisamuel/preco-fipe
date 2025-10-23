package fipe.preco.preco_fipe.response;

import lombok.Builder;

import java.util.List;

@Builder
public record FipeInformationResponse(String brand, String codeFipe, String fuel, String fuelAcronym, String model,
                                      String modelYear, String price, String referenceMonth, int vehicleType,
                                      List<PriceHistory> priceHistories) {
}
