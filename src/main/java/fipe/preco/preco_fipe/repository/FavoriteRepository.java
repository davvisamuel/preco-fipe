package fipe.preco.preco_fipe.repository;

import fipe.preco.preco_fipe.domain.Favorite;
import fipe.preco.preco_fipe.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    Page<Favorite> findAllByUser(Pageable pageable, User user);

    @Transactional
    void deleteFavoriteByIdAndUser(Integer id, User user);
}
