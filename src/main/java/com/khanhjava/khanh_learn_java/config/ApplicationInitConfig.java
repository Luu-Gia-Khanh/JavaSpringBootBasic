package com.khanhjava.khanh_learn_java.config;

import com.khanhjava.khanh_learn_java.entity.User;
import com.khanhjava.khanh_learn_java.enums.RoleEnum;
import com.khanhjava.khanh_learn_java.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            System.out.println("Application started successfully!");
            HashSet<String> roles = new HashSet<>();
            roles.add(RoleEnum.ADMIN.name());
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("admin"))
                                .firstName("Admin")
                                .lastName("Super")
//                                .roles(roles)
                                .build();
                userRepository.save(user);
            }
        };
    }
}
