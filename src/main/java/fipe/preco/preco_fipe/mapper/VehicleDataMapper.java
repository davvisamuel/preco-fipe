package fipe.preco.preco_fipe.mapper;

import fipe.preco.preco_fipe.domain.Fuel;
import fipe.preco.preco_fipe.domain.VehicleData;
import fipe.preco.preco_fipe.domain.VehicleType;
import fipe.preco.preco_fipe.response.FipeInformationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehicleDataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fuel", source = "fuel")
    @Mapping(target = "vehicleType", source = "vehicleType")
    VehicleData toVehicleData(FipeInformationResponse fipeInformationResponse, Fuel fuel, VehicleType vehicleType);
}
