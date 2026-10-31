package com.hospital.management.repository;

import com.hospital.management.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByActiveTrue();

    Optional<Patient> findByIdAndActiveTrue(Long id);

    // ✅ Added for profile completion check
    Optional<Patient> findByUserId(Long userId);

    long countByActiveTrue();
}