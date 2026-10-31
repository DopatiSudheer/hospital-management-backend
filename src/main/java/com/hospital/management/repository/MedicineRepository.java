package com.hospital.management.repository;

import com.hospital.management.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
    List<Medicine> findByActiveTrue();
    
    Optional<Medicine> findByIdAndActiveTrue(Long id);
    
    Optional<Medicine> findByMedicineNameAndActiveTrue(String medicineName);
}

