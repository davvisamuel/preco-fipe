package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.VehicleData;
import fipe.preco.preco_fipe.domain.VehicleType;
import fipe.preco.preco_fipe.dto.response.FipeInformationResponse;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.mapper.VehicleDataMapper;
import fipe.preco.preco_fipe.repository.VehicleDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleDataService {

    private final VehicleDataRepository vehicleDataRepository;
    private final VehicleDataMapper vehicleDataMapper;
    private final FuelService fuelService;

    public VehicleData saveVehicleData(FipeInformationResponse fipeInformationResponse) {
        var codeFipe = fipeInformationResponse.codeFipe();
        var fuelAcronym = fipeInformationResponse.fuelAcronym();
        var vehicleTypeId = fipeInformationResponse.vehicleType();

        var optionalVehicleData = findByCodeFipe(codeFipe);

        if (optionalVehicleData.isPresent()) {
            return optionalVehicleData.get();
        }

        var fuel = fuelService.findByFuelAcronym(fuelAcronym);

        var vehicleTypeName = VehicleType.getVehicleTypeById(vehicleTypeId).orElse("N/A");

        var vehicleData = vehicleDataMapper.toVehicleData(fipeInformationResponse, fuel, vehicleTypeName);

        return vehicleDataRepository.save(vehicleData);
    }

    public Optional<VehicleData> findByCodeFipe(String codeFipe) {
        return vehicleDataRepository.findByCodeFipe(codeFipe);
    }

    public VehicleData findByCodeFipeThrowsNotFoundException(String codeFipe) {
        return findByCodeFipe(codeFipe).orElseThrow(() -> new NotFoundException("VehicleData not found"));
    }
}
