package com.khanhjava.khanh_learn_java.mapper;

import com.khanhjava.khanh_learn_java.dto.request.UserCreationRequest;
import com.khanhjava.khanh_learn_java.dto.request.UserUpdateRequest;
import com.khanhjava.khanh_learn_java.dto.response.UserResponse;
import com.khanhjava.khanh_learn_java.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponse(List<User> users);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
