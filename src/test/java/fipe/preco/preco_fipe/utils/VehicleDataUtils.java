package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.domain.Fuel;
import fipe.preco.preco_fipe.domain.VehicleData;


public class VehicleDataUtils {

    public static VehicleData newVehicleData() {

        var fuel = Fuel.builder()
                .id(1)
                .fuel("Gasolina")
                .fuelAcronym("G")
                .build();

        return VehicleData.builder()
                .id(1)
                .codeFipe("038003-2")
                .fuel(fuel)
                .brand("Acura")
                .model("Integra GS 1.8")
                .modelYear("1992-1")
                .vehicleType("Carro")
                .build();
    }
}
