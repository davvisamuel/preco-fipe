package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.mapper.FavoriteMapper;
import fipe.preco.preco_fipe.repository.FavoriteRepository;
import fipe.preco.preco_fipe.utils.FavoriteUtils;
import fipe.preco.preco_fipe.utils.UserUtils;
import fipe.preco.preco_fipe.utils.VehicleDataUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private FavoriteMapper favoriteMapper;
    @Mock
    private VehicleDataService vehicleDataService;


    @Test
    @Order(1)
    @DisplayName("save creates favorite when successful")
    void save_CreatesFavorite_WhenSuccessful() {
        var user = UserUtils.newSavedUser();

        var vehicleData = VehicleDataUtils.newVehicleData();

        var codeFipe = "038003-2";
        var modelYear = "1992";

        var favoriteToSave = FavoriteUtils.newFavoriteToSave(user, vehicleData);

        var expectedFavorite = FavoriteUtils.newFavoriteSaved(user, vehicleData);

        BDDMockito.when(vehicleDataService.findByCodeFipeAndModelYearOrThrowsNotFoundException(codeFipe, modelYear)).thenReturn(vehicleData);

        BDDMockito.when(favoriteMapper.toFavorite(user, vehicleData)).thenReturn(favoriteToSave);

        BDDMockito.when(favoriteRepository.save(favoriteToSave)).thenReturn(expectedFavorite);

        var favoriteSaved = favoriteRepository.save(favoriteToSave);

        Assertions.assertThat(favoriteSaved)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .isEqualTo(expectedFavorite);

        Assertions.assertThatNoException()
                .isThrownBy(() -> favoriteService.save(user, codeFipe, modelYear));
    }

    @Test
    @Order(2)
    @DisplayName("save throws NotFoundException when vehicleData not found")
    void save_ThrowsNotFoundException_WhenVehicleDataNotFound() {
        var user = UserUtils.newSavedUser();

        var codeFipe = "999999-9";
        var modelYear = "2099";

        BDDMockito.when(vehicleDataService.findByCodeFipeAndModelYearOrThrowsNotFoundException(codeFipe, modelYear)).thenThrow(new NotFoundException("VehicleData not found"));

        Assertions.assertThatException()
                .isThrownBy(() -> favoriteService.save(user, codeFipe, modelYear))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Order(3)
    @DisplayName("findAllPaginated returns Page<Favorite> when successful")
    void findAllPaginated_ReturnsPageOfFavorites_WhenSuccessful() {
        var savedUser = UserUtils.newSavedUser();

        var vehicleData = VehicleDataUtils.newVehicleData();

        var pageable = Pageable.unpaged();

        var expectedFavoritePage = FavoriteUtils.newFavoritePage(savedUser, vehicleData);

        BDDMockito.when(favoriteRepository.findAllByUser(pageable, savedUser))
                .thenReturn(expectedFavoritePage);

        var actualFavoritePage = favoriteService.findAllPaginated(savedUser, pageable);

        Assertions.assertThat(actualFavoritePage)
                .isNotNull()
                .isEqualTo(expectedFavoritePage);
    }

    @Test
    @Order(4)
    @DisplayName("findById returns favorite when favorite is found")
    void findById_ReturnsFavorite_WhenFavoriteIsFound() {
        var user = UserUtils.newSavedUser();

        var vehicleData = VehicleDataUtils.newVehicleData();

        var favorite = FavoriteUtils.newFavoriteSaved(user, vehicleData);

        var id = favorite.getId();

        BDDMockito.when(favoriteRepository.findById(id)).thenReturn(Optional.of(favorite));

        Assertions.assertThat(favoriteService.findById(id))
                .isEqualTo(favorite);
    }

    @Test
    @Order(5)
    @DisplayName("findById throws NotFoundException when favorite not found")
    void findById_ThrowsNotFoundException_WhenFavoriteNotFound() {

        var id = 999;

        BDDMockito.when(favoriteRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> favoriteService.findById(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Order(6)
    @DisplayName("delete removes favorite when favorite is found")
    void delete_RemovesFavorite_WhenFavoriteIsFound() {
        var user = UserUtils.newSavedUser();

        var vehicleData = VehicleDataUtils.newVehicleData();

        var favorite = FavoriteUtils.newFavoriteSaved(user, vehicleData);

        var id = favorite.getId();

        BDDMockito.when(favoriteRepository.findById(id)).thenReturn(Optional.of(favorite));
        BDDMockito.doNothing().when(favoriteRepository).deleteFavoriteByIdAndUser(id, user);

        Assertions.assertThatNoException().isThrownBy(() -> favoriteService.delete(user, id));
    }

    @Test
    @Order(7)
    @DisplayName("delete throws NotFoundException favorite when favorite not found")
    void delete_ThrowsNotFoundException_WhenFavoriteNotFound() {
        var user = UserUtils.newSavedUser();

        var id = 999;

        BDDMockito.when(favoriteRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> favoriteService.delete(user, id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Order(8)
    @DisplayName("existsFavorite returns true when favorite exists")
    void existsFavorite_ReturnsTrue_WhenFavoriteExists() {
        var user = UserUtils.newSavedUser();

        var codeFipe = "038003-2";

        var modelYear = "1992";

        BDDMockito.when(favoriteRepository.findByUserAndVehicleData_CodeFipeAndVehicleData_ModelYear(user, codeFipe, modelYear))
                .thenReturn(Optional.ofNullable(FavoriteUtils.newFavoriteSaved(UserUtils.newSavedUser(), VehicleDataUtils.newVehicleData())));

        var exists = favoriteService.existsFavorite(user, codeFipe, modelYear);

        Assertions.assertThat(exists).isEqualTo(true);
    }

    @Test
    @Order(9)
    @DisplayName("existsFavorite returns false when favorite not exists")
    void existsFavorite_ReturnsFalse_WhenFavoriteNotExists() {
        var user = UserUtils.newSavedUser();

        var codeFipe = "999999-9";

        var modelYear = "2999";

        BDDMockito.when(favoriteRepository.findByUserAndVehicleData_CodeFipeAndVehicleData_ModelYear(user, codeFipe, modelYear))
                .thenReturn(Optional.empty());

        var exists = favoriteService.existsFavorite(user, codeFipe, modelYear);

        Assertions.assertThat(exists).isEqualTo(false);
    }

}