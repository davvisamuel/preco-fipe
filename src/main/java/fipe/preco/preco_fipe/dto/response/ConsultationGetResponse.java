package fipe.preco.preco_fipe.dto.response;

import fipe.preco.preco_fipe.domain.Comparison;
import fipe.preco.preco_fipe.domain.VehicleData;

public record ConsultationGetResponse(Integer id, Comparison comparison, VehicleData vehicleData, String price,
                                      String referenceMonth) {
}
