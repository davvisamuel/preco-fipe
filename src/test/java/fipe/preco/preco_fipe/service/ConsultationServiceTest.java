package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.mapper.ConsultationMapper;
import fipe.preco.preco_fipe.repository.ConsultationRepository;
import fipe.preco.preco_fipe.utils.*;
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
class ConsultationServiceTest {

    @InjectMocks
    private ConsultationService consultationService;
    @Mock
    private ConsultationRepository consultationRepository;
    @Mock
    private ConsultationMapper consultationMapper;
    @Mock
    private VehicleDataService vehicleDataService;
    @Mock
    private ComparisonService comparisonService;


    @Test
    @DisplayName("saveConsultation creates consultation when successful")
    @Order(1)
    void saveConsultation_CreatesConsultation_WhenSuccessful() {
        var fipeInformationResponse = FipeInformationUtils.newFipeInformationResponse();

        var vehicleData = VehicleDataUtils.newVehicleData();

        var user = UserUtils.newSavedUser();

        var consultation = ConsultationUtils.newConsultationSaved(user, vehicleData);


        BDDMockito.when(vehicleDataService.saveVehicleData(fipeInformationResponse))
                .thenReturn(vehicleData);

        BDDMockito.when(consultationMapper.toConsultation(user, vehicleData, fipeInformationResponse))
                .thenReturn(consultation);

        BDDMockito.when(consultationRepository.save(consultation)).thenReturn(consultation);

        Assertions.assertThatNoException()
                .isThrownBy(() -> consultationService
                        .saveConsultation(user, null, fipeInformationResponse));
    }

    @Test
    @DisplayName("saveConsultation creates consultation of comparison when successful")
    @Order(2)
    void saveConsultation_CreatesConsultationOfComparison_WhenSuccessful() {
        var fipeInformationResponse = FipeInformationUtils.newFipeInformationResponse();

        var vehicleData = VehicleDataUtils.newVehicleData();

        var user = UserUtils.newSavedUser();

        var comparison = ComparisonUtils.newComparisonSaved(user);

        var consultation = ConsultationUtils.newConsultationOfComparisonSaved(user, vehicleData, comparison);

        BDDMockito.when(vehicleDataService.saveVehicleData(fipeInformationResponse))
                .thenReturn(vehicleData);

        BDDMockito.when(consultationMapper.toConsultation(user, vehicleData, fipeInformationResponse))
                .thenReturn(consultation);

        BDDMockito.when(comparisonService.findByIdAndUser(comparison.getId(), user))
                .thenReturn(comparison);

        BDDMockito.when(consultationRepository.save(consultation)).thenReturn(consultation);

        Assertions.assertThatNoException()
                .isThrownBy(() -> consultationService
                        .saveConsultation(user, comparison.getId(), fipeInformationResponse));
    }

    @Test
    @DisplayName("saveConsultation Throws NotFoundException when comparison not found")
    @Order(3)
    void saveConsultation_ThrowsNotFoundException_WhenComparisonNotFound() {
        var fipeInformationResponse = FipeInformationUtils.newFipeInformationResponse();

        var vehicleData = VehicleDataUtils.newVehicleData();

        var user = UserUtils.newSavedUser();

        var consultation = ConsultationUtils.newConsultationSaved(user, vehicleData);

        var comparisonId = 999;

        BDDMockito.when(vehicleDataService.saveVehicleData(fipeInformationResponse))
                .thenReturn(vehicleData);

        BDDMockito.when(consultationMapper.toConsultation(user, vehicleData, fipeInformationResponse))
                .thenReturn(consultation);

        BDDMockito.when(comparisonService.findByIdAndUser(comparisonId, user))
                .thenThrow(new NotFoundException("Comparison not found"));

        Assertions.assertThatException()
                .isThrownBy(() -> consultationService
                        .saveConsultation(user, comparisonId, fipeInformationResponse))
                .isInstanceOf(NotFoundException.class);
    }


    @Test
    @DisplayName("findByIdAndUserAndComparisonIsNull returns consultation when consultation is found")
    @Order(4)
    void findByIdAndUserAndComparisonIsNull_ReturnsConsultation_WhenConsultationIsFound() {
        var user = UserUtils.newSavedUser();

        var vehicleData = VehicleDataUtils.newVehicleData();

        var expectedConsultation = ConsultationUtils.newConsultationSaved(user, vehicleData);

        var consultationId = expectedConsultation.getId();

        BDDMockito.when(consultationRepository
                        .findByIdAndUserAndComparisonIsNull(consultationId, user))
                .thenReturn(Optional.of(expectedConsultation));

        var consultation = consultationService.findByIdAndUserAndComparisonIsNull(consultationId, user);

        Assertions.assertThat(consultation)
                .isNotNull()
                .isEqualTo(expectedConsultation);
    }

    @Test
    @DisplayName("findByIdAndUserAndComparisonIsNull Throws NotFoundException when consultation not found")
    @Order(5)
    void findByIdAndUserAndComparisonIsNull_ThrowsNotFoundException_WhenConsultationNotFound() {
        var consultationId = 999;

        var user = User.builder().build();

        BDDMockito.when(consultationRepository
                        .findByIdAndUserAndComparisonIsNull(consultationId, user))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> consultationService.findByIdAndUserAndComparisonIsNull(consultationId, user))
                .isInstanceOf(NotFoundException.class);
    }

    //findAllPaginated

    @Test
    @DisplayName("delete removes consultation when consultation is found")
    @Order(6)
    void delete_RemovesConsultation_WhenConsultationIsFound() {
        var user = UserUtils.newSavedUser();

        var vehicleData = VehicleDataUtils.newVehicleData();

        var consultationSaved = ConsultationUtils.newConsultationSaved(user, vehicleData);

        var id = consultationSaved.getId();

        BDDMockito.when(consultationRepository.findByIdAndUserAndComparisonIsNull(id, user))
                .thenReturn(Optional.of(consultationSaved));

        BDDMockito.doNothing().when(consultationRepository).delete(consultationSaved);

        Assertions.assertThatNoException()
                .isThrownBy(() -> consultationService.delete(id, user));
    }

    @Test
    @DisplayName("delete Throws NotFoundException when consultation not found")
    @Order(7)
    void delete_ThrowsNotFoundException_WhenConsultationNotFound() {
        var id = 99;

        var user = User.builder().build();

        BDDMockito.when(consultationRepository.findByIdAndUserAndComparisonIsNull(id, user))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> consultationService.delete(id, user))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("deleteAllByUser remove all user consultations")
    @Order(8)
    void deleteAllByUser_RemoveAllUserConsultations_WhenSuccessful() {
        var user = User.builder().build();

        BDDMockito.doNothing().when(consultationRepository).
                deleteAllByUserAndComparisonIsNull(user);

        Assertions.assertThatNoException()
                .isThrownBy(() -> consultationService.deleteAllByUser(user));
    }
}