package fipe.preco.preco_fipe.domain;

import java.util.Optional;

public enum VehicleType {
    CARRO(1, "Carro"),
    MOTO(2, "Moto"),
    CAMINHAO(3, "Caminhão");

    final int vehicleType;
    final String vehicleTypeName;

    VehicleType(int vehicleType, String vehicleTypeName) {
        this.vehicleType = vehicleType;
        this.vehicleTypeName = vehicleTypeName;
    }

    public static Optional<String> getVehicleTypeById(int vehicleType) {
        for (VehicleType value : values()) {
            if (value.vehicleType == vehicleType) {
                return Optional.of(value.vehicleTypeName);
            }
        }
        return Optional.empty();
    }

}
