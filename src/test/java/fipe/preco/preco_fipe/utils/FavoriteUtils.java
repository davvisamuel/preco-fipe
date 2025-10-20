package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.domain.Favorite;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.domain.VehicleData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavoriteUtils {
    public Favorite newFavoriteToSave(User user, VehicleData vehicleData) {

        return Favorite.builder()
                .user(user)
                .vehicleData(vehicleData)
                .build();
    }

    public Favorite newFavoriteSaved(User user, VehicleData vehicleData) {

        return Favorite.builder()
                .id(1)
                .user(user)
                .vehicleData(vehicleData)
                .build();
    }

    public List<Favorite> newFavoriteList(User user, VehicleData vehicleData) {
        var favorite1 = Favorite.builder()
                .id(1)
                .user(user)
                .vehicleData(vehicleData)
                .build();

        var favorite2 = Favorite.builder()
                .id(2)
                .user(user)
                .vehicleData(vehicleData)
                .build();

        return List.of(favorite1, favorite2);
    }

    public Page<Favorite> newFavoritePage(User user, VehicleData vehicleData) {
        var pageable = Pageable.unpaged();

        var favorites = newFavoriteList(user, vehicleData);

        return new PageImpl<>(favorites, pageable, 10);
    }
}
