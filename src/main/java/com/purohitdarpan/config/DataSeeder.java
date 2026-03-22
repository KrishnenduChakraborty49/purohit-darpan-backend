package com.purohitdarpan.config;

import com.purohitdarpan.entity.*;
import com.purohitdarpan.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Loads development seed data on startup when profile=dev.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final PujaRepository pujaRepo;
    private final PanchangCacheRepository panchangRepo;
    private final HinduFestivalRepository festivalRepo;

    @Override
    public void run(String... args) {
        if (userRepo.count() == 0) {
            seedUsers();
        }
        if (panchangRepo.count() == 0) {
            seedPanchangForNextMonth();
        }
        log.info("DataSeeder: Seed data initialisation complete.");
    }

    private void seedUsers() {
        log.info("Seeding admin and demo users...");

        User admin = User.builder()
                .username("admin")
                .email("admin@purohitdarpan.com")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .fullName("Platform Admin")
                .role(User.Role.ADMIN)
                .isActive(true)
                .build();

        User mentor = User.builder()
                .username("guru_sharma")
                .email("guru@purohitdarpan.com")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .fullName("Pt. Rajesh Sharma")
                .role(User.Role.MENTOR)
                .isActive(true)
                .build();

        User student = User.builder()
                .username("arjun_student")
                .email("arjun@example.com")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .fullName("Arjun Mishra")
                .role(User.Role.STUDENT)
                .isActive(true)
                .build();

        userRepo.save(admin);
        userRepo.save(mentor);
        userRepo.save(student);

        log.info("Seeded 3 users (admin, mentor, student)");
    }

    private void seedPanchangForNextMonth() {
        log.info("Seeding Panchang for next 30 days (minimal mock data)...");
        // Full panchang population is done lazily by PanchangService on first GET request.
        // Here we just log that it will be populated on demand.
        log.info("Panchang cache will be populated on first API request.");
    }
}
