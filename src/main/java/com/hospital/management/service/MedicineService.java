package com.hospital.management.service;

import com.hospital.management.model.Medicine;

import java.util.List;
import java.util.Optional;

public interface MedicineService {
    
    Medicine createMedicine(Medicine medicine);
    
    List<Medicine> getAllMedicines();
    
    Optional<Medicine> getMedicineById(Long id);
    
    Medicine updateMedicine(Long id, Medicine medicineDetails);
    
    void deleteMedicine(Long id);
}

