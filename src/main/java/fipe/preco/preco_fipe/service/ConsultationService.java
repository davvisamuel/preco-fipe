package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.Consultation;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.mapper.ConsultationMapper;
import fipe.preco.preco_fipe.repository.ConsultationRepository;
import fipe.preco.preco_fipe.response.FipeInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultationService {
    private final ConsultationRepository consultationRepository;
    private final ConsultationMapper consultationMapper;
    private final VehicleDataService vehicleDataService;
    private final ComparisonService comparisonService;

    public Page<Consultation> findAllPaginated(Pageable pageable) {
        return consultationRepository.findAllByComparisonIsNull(pageable);
    }

    public void delete(Integer id, User user) {
        var consultationToDelete = findByIdAndUserAndComparisonIsNull(id, user);
        consultationRepository.delete(consultationToDelete);
    }

    public void deleteAllByUser(User user) {
        consultationRepository.deleteAllByUserAndComparisonIsNull(user);
    }

    public void saveConsultation(User user, Integer comparisonId, FipeInformationResponse fipeInformationResponse) {
        var vehicleData = vehicleDataService.saveVehicleData(fipeInformationResponse);

        var comparison = comparisonService.findByIdAndUser(comparisonId, user);

        var consultation = consultationMapper.toConsultation(user, comparison, vehicleData, fipeInformationResponse);

        consultationRepository.save(consultation);
    }

    public Consultation findByIdAndUserAndComparisonIsNull(Integer id, User user) {
        return consultationRepository.findByIdAndUserAndComparisonIsNull(id, user).orElseThrow(() -> new NotFoundException("Consultation not found"));
    }
}
