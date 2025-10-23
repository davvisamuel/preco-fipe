package fipe.preco.preco_fipe.response;

import fipe.preco.preco_fipe.domain.Consultation;

import java.time.LocalDateTime;
import java.util.Set;

public record ComparisonGetResponse(Integer id, LocalDateTime createdAt,
                                    Set<Consultation> consultations) {
}
