package com.khanhjava.khanh_learn_java.service;

import com.khanhjava.khanh_learn_java.dto.request.UserCreationRequest;
import com.khanhjava.khanh_learn_java.dto.request.UserUpdateRequest;
import com.khanhjava.khanh_learn_java.dto.response.UserResponse;
import com.khanhjava.khanh_learn_java.entity.Role;
import com.khanhjava.khanh_learn_java.entity.User;
import com.khanhjava.khanh_learn_java.enums.RoleEnum;
import com.khanhjava.khanh_learn_java.exception.AppException;
import com.khanhjava.khanh_learn_java.exception.ErrorCode;
import com.khanhjava.khanh_learn_java.mapper.UserMapper;
import com.khanhjava.khanh_learn_java.repository.RoleRepository;
import com.khanhjava.khanh_learn_java.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        boolean isExistUser = userRepository.existsByUsername(request.getUsername());
        if (isExistUser) {
            throw new AppException(ErrorCode.USER_EXIST);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(RoleEnum.USER.name());
//        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public User updateUser(UserUpdateRequest request, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<Role>(roles));
        return userRepository.save(user);
    }

    public List<UserResponse> getUsers() {
        return userMapper.toUserResponse(userRepository.findAll());
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public UserResponse getMeInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                                  .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }
}
