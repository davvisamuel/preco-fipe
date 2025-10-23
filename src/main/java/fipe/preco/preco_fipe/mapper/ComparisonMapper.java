package fipe.preco.preco_fipe.mapper;

import fipe.preco.preco_fipe.domain.Comparison;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.response.ComparisonGetResponse;
import fipe.preco.preco_fipe.response.ComparisonPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComparisonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comparison toComparison(User user);

    ComparisonPostResponse toComparisonPostResponse(Comparison comparison);

    ComparisonGetResponse toComparisonGetResponse(Comparison comparison);
}
