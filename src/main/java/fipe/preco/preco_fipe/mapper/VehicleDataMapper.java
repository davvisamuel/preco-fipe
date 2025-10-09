package fipe.preco.preco_fipe.mapper;

import fipe.preco.preco_fipe.domain.VehicleData;
import fipe.preco.preco_fipe.response.FipeInformationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehicleDataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicleType", ignore = true)
    @Mapping(target = "fuel", ignore = true)
    VehicleData toVehicleData(FipeInformationResponse fipeInformationResponse);
}
