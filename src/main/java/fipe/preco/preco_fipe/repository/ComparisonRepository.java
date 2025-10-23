package fipe.preco.preco_fipe.repository;

import fipe.preco.preco_fipe.domain.Comparison;
import fipe.preco.preco_fipe.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComparisonRepository extends JpaRepository<Comparison, Integer> {

    Optional<Comparison> findByIdAndUser(Integer id, User user);

    Page<Comparison> findAllByUser(User user, Pageable pageable);
}
