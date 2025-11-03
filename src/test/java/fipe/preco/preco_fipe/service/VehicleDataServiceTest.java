package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.VehicleType;
import fipe.preco.preco_fipe.dto.response.FipeInformationResponse;
import fipe.preco.preco_fipe.mapper.VehicleDataMapper;
import fipe.preco.preco_fipe.repository.VehicleDataRepository;
import fipe.preco.preco_fipe.utils.FipeInformationUtils;
import fipe.preco.preco_fipe.utils.VehicleDataUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VehicleDataServiceTest {

    @InjectMocks
    VehicleDataService vehicleDataService;

    @InjectMocks
    VehicleDataUtils vehicleDataUtils;

    @InjectMocks
    FipeInformationUtils fipeInformationUtils;

    @Mock
    VehicleDataRepository vehicleDataRepository;

    @Mock
    VehicleDataMapper vehicleDataMapper;

    @Mock
    FuelService fuelService;

    @Test
    @DisplayName("saveVehicleData returns vehicleData when exists")
    @Order(1)
    void saveVehicleData_ReturnsVehicleData_WhenExists() {
        var fipeInformationResponse = fipeInformationUtils.newFipeInformationResponse();

        var vehicleData = vehicleDataUtils.newVehicleData();

        var codeFipe = vehicleData.getCodeFipe();

        BDDMockito.when(vehicleDataRepository.findByCodeFipe(codeFipe)).thenReturn(Optional.of(vehicleData));

        var response = vehicleDataService.saveVehicleData(fipeInformationResponse);

        Assertions.assertThat(response)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .isEqualTo(vehicleData);
    }

    @Test
    @DisplayName("saveVehicleData creates vehicleData when not exists")
    @Order(2)
    void saveVehicleData_CreatesVehicleData_WhenNotExists() {
        var fipeInformationResponse = fipeInformationUtils.newFipeInformationResponse();

        var codeFipe = fipeInformationResponse.codeFipe();

        var fuelAcronym = fipeInformationResponse.fuelAcronym();

        BDDMockito.when(vehicleDataRepository.findByCodeFipe(codeFipe)).thenReturn(Optional.empty());

        var vehicleData = vehicleDataUtils.newVehicleData();

        var fuelSaved = vehicleData.getFuel();

        var vehicleTypeName = "Carro";

        BDDMockito.when(fuelService.findByFuelAcronym(fuelAcronym)).thenReturn(fuelSaved);

        BDDMockito.when(vehicleDataMapper.toVehicleData(fipeInformationResponse, fuelSaved, vehicleTypeName)).thenReturn(vehicleData);

        BDDMockito.when(vehicleDataRepository.save(vehicleData)).thenReturn(vehicleData);

        var response = vehicleDataService.saveVehicleData(fipeInformationResponse);

        Assertions.assertThat(response)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .isEqualTo(vehicleData);
    }


}