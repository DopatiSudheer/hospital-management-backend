package com.hospital.management.service;

import com.hospital.management.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {
    
    MedicalRecord createMedicalRecord(MedicalRecord medicalRecord);
    
    List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId);
    
    List<MedicalRecord> getAllMedicalRecords();
}

