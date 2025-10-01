package com.khanhjava.khanh_learn_java.service;

import com.khanhjava.khanh_learn_java.dto.request.RoleRequest;
import com.khanhjava.khanh_learn_java.dto.response.RoleResponse;
import com.khanhjava.khanh_learn_java.entity.Permission;
import com.khanhjava.khanh_learn_java.entity.Role;
import com.khanhjava.khanh_learn_java.mapper.RoleMapper;
import com.khanhjava.khanh_learn_java.repository.PermissionRepository;
import com.khanhjava.khanh_learn_java.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<Permission>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toRoleResponse(roles);
    }

    public void delete(String name) {
        roleRepository.deleteById(name);
    }
}
