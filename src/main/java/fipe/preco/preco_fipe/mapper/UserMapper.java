package fipe.preco.preco_fipe.mapper;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.request.UserPostRequest;
import fipe.preco.preco_fipe.dto.request.UserPutRequest;
import fipe.preco.preco_fipe.dto.response.UserGetResponse;
import fipe.preco.preco_fipe.dto.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toUser(UserPostRequest userPostRequest);

    UserPostResponse toUserPostResponse(User user);

    UserGetResponse toUserGetResponse(User user);

    User toUser(UserPutRequest userPutRequest);
}
