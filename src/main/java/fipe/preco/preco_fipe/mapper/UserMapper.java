package fipe.preco.preco_fipe.mapper;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.request.UserPostRequest;
import fipe.preco.preco_fipe.request.UserPutRequest;
import fipe.preco.preco_fipe.response.UserGetResponse;
import fipe.preco.preco_fipe.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "roles", constant = "USER")
    User toUser(UserPostRequest userPostRequest);

    UserPostResponse toUserPostResponse(User user);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> users);

    User toUser(UserPutRequest userPutRequest);
}
