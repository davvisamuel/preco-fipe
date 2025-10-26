package fipe.preco.preco_fipe.mapper;

import fipe.preco.preco_fipe.domain.Consultation;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.domain.VehicleData;
import fipe.preco.preco_fipe.response.ConsultationGetResponse;
import fipe.preco.preco_fipe.response.FipeInformationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConsultationMapper {

    @Mapping(target = "id", ignore = true)
    Consultation toConsultation(User user, VehicleData vehicleData, FipeInformationResponse fipeInformationResponse);

    ConsultationGetResponse toConsultationGetResponse(Consultation consultation);
}
