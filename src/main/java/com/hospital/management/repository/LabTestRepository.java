package com.hospital.management.repository;

import com.hospital.management.model.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    
    List<LabTest> findByActiveTrue();
    
    Optional<LabTest> findByIdAndActiveTrue(Long id);
    
    List<LabTest> findByPatientIdAndActiveTrue(Long patientId);
}

