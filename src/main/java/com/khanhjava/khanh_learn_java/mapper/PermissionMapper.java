package com.khanhjava.khanh_learn_java.mapper;

import com.khanhjava.khanh_learn_java.dto.request.PermissionRequest;
import com.khanhjava.khanh_learn_java.dto.response.PermissionResponse;
import com.khanhjava.khanh_learn_java.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    List<PermissionResponse> toPermissionResponse(List<Permission> permissions);

    void updatePermission(@MappingTarget Permission permission, PermissionRequest request);
}
