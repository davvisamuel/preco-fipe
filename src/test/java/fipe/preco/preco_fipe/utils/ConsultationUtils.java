package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.domain.Comparison;
import fipe.preco.preco_fipe.domain.Consultation;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.domain.VehicleData;

public class ConsultationUtils {

    public static Consultation newConsultationSaved(User user, VehicleData vehicleData) {

        return Consultation.builder()
                .id(1)
                .user(user)
                .vehicleData(vehicleData)
                .referenceMonth("novembro de 2025")
                .price("R$ 10.980,00")
                .build();
    }

    public static Consultation newConsultationOfComparisonSaved(User user, VehicleData vehicleData, Comparison comparison) {

        return Consultation.builder()
                .id(1)
                .user(user)
                .comparison(comparison)
                .vehicleData(vehicleData)
                .referenceMonth("novembro de 2025")
                .price("R$ 10.980,00")
                .build();
    }
}
