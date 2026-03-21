package com.techblog.config;

import com.techblog.common.enums.CategoryType;
import com.techblog.common.enums.RoleName;
import com.techblog.common.enums.UserStatus;
import com.techblog.domain.category.model.Category;
import com.techblog.domain.category.repository.CategoryRepository;
import com.techblog.domain.user.model.Role;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.model.UserRole;
import com.techblog.domain.user.repository.RoleRepository;
import com.techblog.domain.user.repository.UserRepository;
import com.techblog.domain.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedRoles();
        seedAdmin();
        seedCategories();
    }

    private void seedRoles() {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleName);
                role.setDescription("System role: " + roleName.name());
                return roleRepository.save(role);
            });
        }
    }

    private void seedAdmin() {
        if (userRepository.existsByEmail("admin@gmail.com")) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPasswordHash(passwordEncoder.encode("123456"));

        // THÊM DÒNG NÀY ĐỂ SỬA LỖI
        admin.setDisplayName("System Admin");

        admin.setStatus(UserStatus.ACTIVE);

        // Cẩn thận hơn, bạn có thể set luôn emailVerified là true cho Admin
        admin.setEmailVerified(true);

        admin = userRepository.save(admin);

        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        UserRole userRole = new UserRole();
        userRole.setUser(admin);
        userRole.setRole(adminRole);
        userRoleRepository.save(userRole);
    }

    private void seedCategories() {
        if (!categoryRepository.existsBySlug("dien-thoai")) {
            Category c = new Category();
            c.setName("Điện thoại");
            c.setSlug("dien-thoai");
            c.setType(CategoryType.PRODUCT);
            c.setActive(true);
            categoryRepository.save(c);
        }

        if (!categoryRepository.existsBySlug("tin-cong-nghe")) {
            Category c = new Category();
            c.setName("Tin công nghệ");
            c.setSlug("tin-cong-nghe");
            c.setType(CategoryType.POST);
            c.setActive(true);
            categoryRepository.save(c);
        }
    }
}