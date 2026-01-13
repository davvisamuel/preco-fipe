package fipe.preco.preco_fipe.dto.message;

import fipe.preco.preco_fipe.dto.response.FipeInformationResponse;

public record ConsultationProducerRequest(Integer userId, Integer comparisonId,
                                          FipeInformationResponse fipeInformationResponse, String year) {
}
