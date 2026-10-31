package com.hospital.management.service;

import com.hospital.management.model.Medicine;
import com.hospital.management.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public Medicine createMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @Override
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findByActiveTrue();
    }

    @Override
    public Optional<Medicine> getMedicineById(Long id) {
        return medicineRepository.findByIdAndActiveTrue(id);
    }

    @Override
    public Medicine updateMedicine(Long id, Medicine medicineDetails) {
        return medicineRepository.findByIdAndActiveTrue(id).map(medicine -> {
            medicine.setMedicineName(medicineDetails.getMedicineName());
            medicine.setPrice(medicineDetails.getPrice());
            medicine.setStock(medicineDetails.getStock());
            return medicineRepository.save(medicine);
        }).orElse(null);
    }

    @Override
    public void deleteMedicine(Long id) {
        medicineRepository.findByIdAndActiveTrue(id).ifPresent(medicine -> {
            medicine.setActive(false);
            medicineRepository.save(medicine);
        });
    }
}

