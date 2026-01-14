package fipe.preco.preco_fipe.repository;

import fipe.preco.preco_fipe.domain.Consultation;
import fipe.preco.preco_fipe.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {

    Page<Consultation> findAllByUserAndComparisonIsNull(User user, Pageable pageable);

    Optional<Consultation> findByIdAndUserAndComparisonIsNull(Integer integer, User user);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM preco_fipe.consultation WHERE user_id = :userId AND comparison_id IS NULL", nativeQuery = true)
    void deleteAllByUserAndComparisonIsNull(@Param("userId") Integer userId);
}
