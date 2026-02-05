package fipe.preco.preco_fipe.repository;

import fipe.preco.preco_fipe.domain.RefreshToken;
import fipe.preco.preco_fipe.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
