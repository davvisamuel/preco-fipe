package fipe.preco.preco_fipe.request;

import fipe.preco.preco_fipe.response.FipeInformationResponse;

public record ConsultationProducerRequest(Integer userId, Integer comparisonId,
                                          FipeInformationResponse fipeInformationResponse) {
}
