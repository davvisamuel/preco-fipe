package fipe.preco.preco_fipe.mapper;

import fipe.preco.preco_fipe.request.ConsultationProducerRequest;
import fipe.preco.preco_fipe.response.FipeInformationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConsultationProducerMapper {

    ConsultationProducerRequest toConsultationProducerRequest(Integer userId, Integer comparisonId, FipeInformationResponse fipeInformationResponse);
}
