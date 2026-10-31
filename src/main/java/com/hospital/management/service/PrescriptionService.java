package com.hospital.management.service;

import com.hospital.management.model.Prescription;

import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    
    Prescription createPrescription(Prescription prescription);
    
    List<Prescription> getAllPrescriptions();
    
    Optional<Prescription> getPrescriptionById(Long id);
    
    Prescription updatePrescription(Long id, Prescription prescriptionDetails);
    
    void deletePrescription(Long id);
}

