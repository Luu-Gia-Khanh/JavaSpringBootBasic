package com.khanhjava.khanh_learn_java.mapper;

import com.khanhjava.khanh_learn_java.dto.request.RoleRequest;
import com.khanhjava.khanh_learn_java.dto.response.RoleResponse;
import com.khanhjava.khanh_learn_java.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toRoleResponse(List<Role> permissions);
}
