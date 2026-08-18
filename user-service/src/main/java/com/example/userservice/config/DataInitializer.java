package com.example.userservice.config;

import com.example.userservice.entity.Role;
import com.example.userservice.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeRoles(RoleRepository roleRepository) {

        return args -> {

            if (roleRepository.findByRoleName("ADMIN").isEmpty()) {
                Role admin = new Role();
                admin.setRoleName("ADMIN");
                roleRepository.save(admin);
            }

            if (roleRepository.findByRoleName("INSTRUCTOR").isEmpty()) {
                Role instructor = new Role();
                instructor.setRoleName("INSTRUCTOR");
                roleRepository.save(instructor);
            }

            if (roleRepository.findByRoleName("STUDENT").isEmpty()) {
                Role student = new Role();
                student.setRoleName("STUDENT");
                roleRepository.save(student);
            }

            System.out.println("Roles initialized successfully!");
        };
    }
}