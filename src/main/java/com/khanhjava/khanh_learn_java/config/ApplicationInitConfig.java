package com.khanhjava.khanh_learn_java.config;

import com.khanhjava.khanh_learn_java.entity.Permission;
import com.khanhjava.khanh_learn_java.entity.Role;
import com.khanhjava.khanh_learn_java.entity.User;
import com.khanhjava.khanh_learn_java.repository.RoleRepository;
import com.khanhjava.khanh_learn_java.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public ApplicationInitConfig(PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Bean
    @ConditionalOnProperty(prefix = "spring", value = "datasource.driverClassName", havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            System.out.println("Application started successfully!");
            Permission permission = Permission.builder()
                                              .name("APPROVE_POST")
                                              .description("Approve post permission")
                                              .build();

            Role role = Role.builder()
                            .name("ADMIN")
                            .description("ADMIN")
                            .permissions(Set.of(permission))
                            .build();
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("admin"))
                                .firstName("Admin")
                                .lastName("Super")
                                .roles(Set.of(role))
                                .build();
                userRepository.save(user);
            }
        };
    }
}
