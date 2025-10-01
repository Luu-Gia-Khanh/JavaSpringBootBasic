package com.khanhjava.khanh_learn_java.service;

import com.khanhjava.khanh_learn_java.dto.request.PermissionRequest;
import com.khanhjava.khanh_learn_java.dto.response.PermissionResponse;
import com.khanhjava.khanh_learn_java.entity.Permission;
import com.khanhjava.khanh_learn_java.mapper.PermissionMapper;
import com.khanhjava.khanh_learn_java.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissionMapper.toPermissionResponse(permissions);
    }

    public void delete(String name) {
        permissionRepository.deleteById(name);
    }
}
