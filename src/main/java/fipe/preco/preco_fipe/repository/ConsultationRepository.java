package fipe.preco.preco_fipe.repository;

import fipe.preco.preco_fipe.domain.Consultation;
import fipe.preco.preco_fipe.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {

    Page<Consultation> findAllByUserAndComparisonIsNull(User user, Pageable pageable);

    Optional<Consultation> findByIdAndUserAndComparisonIsNull(Integer integer, User user);

    void deleteAllByUserAndComparisonIsNull(User user);
}
