package com.hospital.management.controller;

import com.hospital.management.dto.ApiResponse;
import com.hospital.management.dto.MedicalRecordRequest;
import com.hospital.management.model.MedicalRecord;
import com.hospital.management.model.Patient;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping
    public ResponseEntity<ApiResponse> createMedicalRecord(@RequestBody MedicalRecordRequest request) {
        try {
            // Validate required fields
            if (request.getVisitDate() == null ||
                request.getSymptoms() == null || request.getSymptoms().trim().isEmpty() ||
                request.getDiagnosis() == null || request.getDiagnosis().trim().isEmpty() ||
                request.getTreatment() == null || request.getTreatment().trim().isEmpty() ||
                request.getPatientId() == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Visit date, symptoms, diagnosis, treatment, and patient ID are required"));
            }

            // Validate patient exists
            Optional<Patient> patientOptional = patientRepository.findById(request.getPatientId());
            if (patientOptional.isEmpty() || !patientOptional.get().getActive()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Patient not found or inactive"));
            }

            // Create medical record
            MedicalRecord medicalRecord = new MedicalRecord();
            medicalRecord.setVisitDate(request.getVisitDate());
            medicalRecord.setSymptoms(request.getSymptoms().trim());
            medicalRecord.setDiagnosis(request.getDiagnosis().trim());
            medicalRecord.setTreatment(request.getTreatment().trim());
            medicalRecord.setPatient(patientOptional.get());

            MedicalRecord createdRecord = medicalRecordService.createMedicalRecord(medicalRecord);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Medical record created successfully", createdRecord));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to create medical record: " + e.getMessage()));
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse> getMedicalRecordsByPatientId(@PathVariable Long patientId) {
        try {
            // Validate patient exists
            Optional<Patient> patientOptional = patientRepository.findById(patientId);
            if (patientOptional.isEmpty() || !patientOptional.get().getActive()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Patient not found or inactive"));
            }

            List<MedicalRecord> records = medicalRecordService.getMedicalRecordsByPatientId(patientId);
            return ResponseEntity.ok(new ApiResponse(true, "Medical records fetched successfully", records));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to fetch medical records: " + e.getMessage()));
        }
    }
}

