package fipe.preco.preco_fipe.repository;

import fipe.preco.preco_fipe.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndIdNot(String email, Integer id);
}
