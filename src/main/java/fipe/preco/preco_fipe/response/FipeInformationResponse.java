package fipe.preco.preco_fipe.response;

import java.util.List;

public record FipeInformationResponse(String brand, String codeFipe, String fuel, String fuelAcronym, String model,
                                      String modelYear, String price, String referenceMonth, String vehicleType,
                                      List<PriceHistory> priceHistories) {
}
