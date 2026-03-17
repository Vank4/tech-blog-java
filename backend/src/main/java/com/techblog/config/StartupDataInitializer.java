package com.techblog.config;

import com.techblog.common.enums.RoleName;
import com.techblog.domain.user.model.Role;
import com.techblog.domain.user.repository.RoleRepository;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        Arrays.stream(RoleName.values()).forEach(this::ensureRoleExists);
    }

    private void ensureRoleExists(RoleName roleName) {
        roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            role.setDescription("System role " + roleName.name());
            log.info("Seeding role: {}", roleName.name());
            return roleRepository.save(role);
        });
    }
}
