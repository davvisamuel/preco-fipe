package fipe.preco.preco_fipe.mapper;

import fipe.preco.preco_fipe.domain.Favorite;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.domain.VehicleData;
import fipe.preco.preco_fipe.response.FavoriteGetResponse;
import fipe.preco.preco_fipe.response.FavoritePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FavoriteMapper {

    @Mapping(target = "id", ignore = true)
    Favorite toFavorite(User user, VehicleData vehicleData);

    FavoritePostResponse toFavoritePostResponse(Favorite favorite);

    FavoriteGetResponse toFavoriteGetResponse(Favorite favorite);
}
