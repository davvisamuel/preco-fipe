package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.VehicleData;
import fipe.preco.preco_fipe.mapper.VehicleDataMapper;
import fipe.preco.preco_fipe.repository.ConsultationRepository;
import fipe.preco.preco_fipe.repository.FuelRepository;
import fipe.preco.preco_fipe.repository.VehicleDataRepository;
import fipe.preco.preco_fipe.repository.VehicleTypeRepository;
import fipe.preco.preco_fipe.response.FipeInformationResponse;
import fipe.preco.preco_fipe.utils.HistoryUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @InjectMocks
    private HistoryService historyService;
    @InjectMocks
    private HistoryUtils historyUtils;
    @Mock
    private VehicleDataRepository vehicleDataRepository;
    @Mock
    private ConsultationRepository consultationRepository;
    @Mock
    private FuelRepository fuelRepository;
    @Mock
    private VehicleTypeRepository vehicleTypeRepository;
    @Mock
    private VehicleDataMapper vehicleDataMapper;

    @Test
    @Order(1)
    @DisplayName("saveVehicleData returns vehicleData when vehicle data exists")
    void saveVehicleData_ReturnsVehicleData_WhenVehicleDataExists() {
        var fipeInformationResponse = historyUtils.newFipeInformationResponse();

        var vehicleData = historyUtils.newVehicleData();

        var codeFipe = fipeInformationResponse.codeFipe();

        BDDMockito.when(vehicleDataRepository.findByCodeFipe(codeFipe)).thenReturn(Optional.of(vehicleData));

        Assertions.assertThat(historyService.saveVehicleData(fipeInformationResponse))
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .isEqualTo(vehicleData);
    }

    @Test
    @Order(2)
    @DisplayName("saveVehicleData creates vehicleData when vehicle data not exists")
    void saveVehicleData_CreatesVehicleData_WhenVehicleDataNotExists() {
        var fipeInformationResponse = historyUtils.newFipeInformationResponse();

        var codeFipe = fipeInformationResponse.codeFipe();
        BDDMockito.when(vehicleDataRepository.findByCodeFipe(codeFipe)).thenReturn(Optional.empty());

        var fuelAcronym = fipeInformationResponse.fuelAcronym();
        var fuelSaved = historyUtils.newFuel();
        BDDMockito.when(fuelRepository.findByFuelAcronym(fuelAcronym)).thenReturn(Optional.of(fuelSaved));

        var vehicleType = Integer.parseInt(fipeInformationResponse.vehicleType());
        var vehicleTypeSaved = historyUtils.newVehicleType();
        BDDMockito.when(vehicleTypeRepository.findById(vehicleType)).thenReturn(Optional.of(vehicleTypeSaved));

        var vehicleData = historyUtils.newVehicleData();
        BDDMockito.when(vehicleDataMapper.toVehicleData(fipeInformationResponse, fuelSaved, vehicleTypeSaved)).thenReturn(vehicleData);

        BDDMockito.when(vehicleDataRepository.save(vehicleData)).thenReturn(vehicleData);

        Assertions.assertThat(historyService.saveVehicleData(fipeInformationResponse))
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .isEqualTo(vehicleData);
    }
}