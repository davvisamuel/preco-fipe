package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.domain.Fuel;
import fipe.preco.preco_fipe.domain.VehicleData;
import fipe.preco.preco_fipe.dto.response.FipeInformationResponse;
import org.springframework.stereotype.Component;

@Component
public class HistoryUtils {

    public FipeInformationResponse newFipeInformationResponse() {
        return FipeInformationResponse.builder()
                .brand("Acura")
                .codeFipe("038003-2")
                .fuel("Gasolina")
                .fuelAcronym("G")
                .model("Integra GS 1.8")
                .modelYear("1992")
                .referenceMonth("outubro de 2025")
                .vehicleType(1)
                .price("R$ 11.007,00")
                .build();
    }

    public Fuel newFuel() {
        return Fuel.builder()
                .id(1)
                .fuel("Gasolina")
                .fuelAcronym("G")
                .build();
    }


    public VehicleData newVehicleData() {
        var fuel = newFuel();

        return VehicleData.builder()
                .id(1)
                .codeFipe("038003-2")
                .fuel(fuel)
                .brand("Acura")
                .model("Integra GS 1.8")
                .modelYear("1992")
                .vehicleType("Carro")
                .build();
    }
}
