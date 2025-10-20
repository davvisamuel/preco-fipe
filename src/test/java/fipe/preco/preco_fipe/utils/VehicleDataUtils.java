package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.domain.Fuel;
import fipe.preco.preco_fipe.domain.VehicleData;
import fipe.preco.preco_fipe.domain.VehicleType;
import org.springframework.stereotype.Component;

@Component
public class VehicleDataUtils {

    public VehicleData newVehicleData() {
        var fuel = Fuel.builder()
                .id(1)
                .fuel("Gasolina")
                .fuelAcronym("G")
                .build();

        var vehicleType = VehicleType.builder()
                .id(1)
                .typeName("Carro")
                .build();

        return VehicleData.builder()
                .id(1)
                .codeFipe("038003-2")
                .fuel(fuel)
                .brand("Acura")
                .model("Integra GS 1.8")
                .modelYear("1992")
                .vehicleType(vehicleType)
                .build();
    }
}
