package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.Favorite;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.mapper.FavoriteMapper;
import fipe.preco.preco_fipe.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final VehicleDataService vehicleDataService;

    public Favorite save(User user, String codeFipe, String modelYear, String fuelAcronym) {
        var vehicleData = vehicleDataService.findByCodeFipeAndModelYearAndFuelAcronymOrThrowsNotFoundException(codeFipe, modelYear, fuelAcronym);

        var favorite = favoriteMapper.toFavorite(user, vehicleData);

        return favoriteRepository.save(favorite);
    }

    public Page<Favorite> findAllPaginated(User user, Pageable pageable) {
        return favoriteRepository.findAllByUser(pageable, user);
    }

    Favorite findById(Integer id) {
        return favoriteRepository.findById(id).orElseThrow(() -> new NotFoundException("Favorite not found"));
    }

    void assertFavoriteExists(Integer id) {
        findById(id);
    }

    public void delete(User user, Integer id) {
        assertFavoriteExists(id);
        favoriteRepository.deleteFavoriteByIdAndUser(id, user);
    }
}
