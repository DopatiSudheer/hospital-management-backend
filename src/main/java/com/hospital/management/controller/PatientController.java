package com.hospital.management.controller;

import com.hospital.management.dto.ApiResponse;
import com.hospital.management.model.Patient;
import com.hospital.management.service.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // =========================================================
    // CREATE PATIENT (Handles BOTH admin + self profile)
    // Endpoint: POST /api/patients?email=...
    // =========================================================
    @PostMapping
    public ResponseEntity<ApiResponse> createPatient(
            @RequestBody Patient patient,
            @RequestParam String email) {

        try {

            if (patient.getPatientName() == null || patient.getPatientName().trim().isEmpty() ||
                patient.getGender() == null || patient.getGender().trim().isEmpty() ||
                patient.getAge() == null ||
                patient.getBloodGroup() == null || patient.getBloodGroup().trim().isEmpty() ||
                patient.getContactNumber() == null || patient.getContactNumber().trim().isEmpty() ||
                patient.getAddress() == null || patient.getAddress().trim().isEmpty()) {

                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "All fields are required"));
            }

            Patient createdPatient = patientService.createPatient(patient, email);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Patient processed successfully", createdPatient));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    // =========================================================
    // GET ALL PATIENTS
    // =========================================================
    @GetMapping
    public ResponseEntity<ApiResponse> getAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.ok(
                    new ApiResponse(true, "Patients retrieved successfully", patients)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to retrieve patients: " + e.getMessage()));
        }
    }

    // =========================================================
    // GET PATIENT BY ID
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPatientById(@PathVariable Long id) {
        try {

            Patient patient = patientService.getPatientById(id);

            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Patient not found"));
            }

            return ResponseEntity.ok(
                    new ApiResponse(true, "Patient retrieved successfully", patient)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to retrieve patient: " + e.getMessage()));
        }
    }

    // =========================================================
    // UPDATE PATIENT
    // =========================================================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient) {

        try {

            Patient updatedPatient = patientService.updatePatient(id, patient);

            if (updatedPatient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Patient not found"));
            }

            return ResponseEntity.ok(
                    new ApiResponse(true, "Patient updated successfully", updatedPatient)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to update patient: " + e.getMessage()));
        }
    }

    // =========================================================
    // DELETE PATIENT (Soft Delete)
    // =========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable Long id) {

        try {

            Patient patient = patientService.getPatientById(id);

            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Patient not found"));
            }

            patientService.deletePatient(id);

            return ResponseEntity.ok(
                    new ApiResponse(true, "Patient deleted successfully")
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to delete patient: " + e.getMessage()));
        }
    }
}