package com.hospital.management.config;

import com.hospital.management.model.User;
import com.hospital.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer {

    @Autowired
    private UserRepository userRepository;

    @Value("${app.admin.email:admin@hospital.com}")
    private String adminEmail;

    @Value("${app.admin.password:admin@123}")
    private String adminPassword;

    @Value("${app.admin.name:System Administrator}")
    private String adminName;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeAdminUser() {
        System.out.println("\n========== ADMIN INITIALIZATION STARTED ==========");
        System.out.println("Checking for admin user with email: " + adminEmail);

        try {
            if (userRepository.findByEmail(adminEmail).isPresent()) {
                System.out.println("✓ Admin user already exists with email: " + adminEmail);
                System.out.println("========== ADMIN INITIALIZATION COMPLETED ==========\n");
                return;
            }

            System.out.println("Admin user not found. Creating new admin user...");

            User adminUser = new User();
            adminUser.setName(adminName);
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(adminPassword);
            adminUser.setRole("ADMIN");
            adminUser.setActive(true);
            adminUser.setFirstLogin(false);

            User savedUser = userRepository.save(adminUser);

            System.out.println("\n✓✓✓ ADMIN USER CREATED SUCCESSFULLY! ✓✓✓");
            System.out.println("  ID: " + savedUser.getId());
            System.out.println("  Email: " + savedUser.getEmail());
            System.out.println("  Role: " + savedUser.getRole());
            System.out.println("\n  📧 Email: admin@hospital.com");
            System.out.println("  🔐 Password: admin@123");
            System.out.println("========== ADMIN INITIALIZATION COMPLETED ==========\n");

        } catch (Exception e) {
            System.err.println("\n✗✗✗ ERROR CREATING ADMIN USER! ✗✗✗");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
