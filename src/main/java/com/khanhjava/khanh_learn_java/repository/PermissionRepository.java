package com.khanhjava.khanh_learn_java.repository;

import com.khanhjava.khanh_learn_java.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
