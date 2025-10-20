package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.domain.VehicleData;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.mapper.ConsultationMapper;
import fipe.preco.preco_fipe.mapper.VehicleDataMapper;
import fipe.preco.preco_fipe.repository.ConsultationRepository;
import fipe.preco.preco_fipe.repository.FuelRepository;
import fipe.preco.preco_fipe.repository.VehicleDataRepository;
import fipe.preco.preco_fipe.repository.VehicleTypeRepository;
import fipe.preco.preco_fipe.response.FipeInformationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {

    private final VehicleDataMapper vehicleDataMapper;
    private final ConsultationMapper consultationMapper;

    private final VehicleDataRepository vehicleDataRepository;
    private final ConsultationRepository consultationRepository;
    private final FuelRepository fuelRepository;
    private final VehicleTypeRepository vehicleTypeRepository;

    public VehicleData saveVehicleData(FipeInformationResponse fipeInformationResponse) {
        var codeFipe = fipeInformationResponse.codeFipe();

        var vehicleDataOptional = vehicleDataRepository.findByCodeFipe(codeFipe);

        if (vehicleDataOptional.isPresent()) {
            return vehicleDataOptional.get();
        }

        var fuel = fuelRepository.findByFuelAcronym(fipeInformationResponse.fuelAcronym()).orElseThrow(() -> new NotFoundException("Vehicle type not found"));
        var vehicleType = vehicleTypeRepository.findById(Integer.parseInt(fipeInformationResponse.vehicleType())).orElseThrow(() -> new NotFoundException("Vehicle type not found"));

        var vehicleData = vehicleDataMapper.toVehicleData(fipeInformationResponse, fuel, vehicleType);

        return vehicleDataRepository.save(vehicleData);
    }

    public void saveConsultation(User user, FipeInformationResponse fipeInformationResponse) {
        var vehicleData = saveVehicleData(fipeInformationResponse);

        var consultation = consultationMapper.toConsultation(user, vehicleData, fipeInformationResponse);

        consultationRepository.save(consultation);
    }
}
