package com.hospital.management.controller;

import com.hospital.management.dto.ApiResponse;
import com.hospital.management.model.Doctor;
import com.hospital.management.service.DoctorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // ================= CREATE DOCTOR =================
    @PostMapping
    public ResponseEntity<ApiResponse> createDoctor(
            @RequestBody Doctor doctor,
            @RequestParam String email) {

        try {

            if (doctor.getDoctorName() == null || doctor.getDoctorName().trim().isEmpty() ||
                doctor.getSpecialization() == null || doctor.getSpecialization().trim().isEmpty() ||
                doctor.getQualification() == null || doctor.getQualification().trim().isEmpty() ||
                doctor.getExperience() == null ||
                doctor.getContactNumber() == null || doctor.getContactNumber().trim().isEmpty()) {

                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "All fields are required"));
            }

            Doctor createdDoctor = doctorService.createDoctor(doctor, email);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(
                            true,
                            "Doctor created successfully. Default password generated.",
                            createdDoctor
                    ));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    // ================= GET ALL DOCTORS =================
    @GetMapping
    public ResponseEntity<ApiResponse> getAllDoctors() {

        try {
            List<Doctor> doctors = doctorService.getAllDoctors();

            return ResponseEntity.ok(
                    new ApiResponse(true, "Doctors retrieved successfully", doctors)
            );

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to retrieve doctors: " + e.getMessage()));
        }
    }

    // ================= GET DOCTOR BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDoctorById(@PathVariable Long id) {

        Doctor doctor = doctorService.getDoctorById(id);

        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Doctor not found"));
        }

        return ResponseEntity.ok(
                new ApiResponse(true, "Doctor retrieved successfully", doctor)
        );
    }

    // ================= UPDATE DOCTOR =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor) {

        Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);

        if (updatedDoctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Doctor not found"));
        }

        return ResponseEntity.ok(
                new ApiResponse(true, "Doctor updated successfully", updatedDoctor)
        );
    }

    // ================= DELETE DOCTOR =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDoctor(@PathVariable Long id) {

        Doctor doctor = doctorService.getDoctorById(id);

        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Doctor not found"));
        }

        doctorService.deleteDoctor(id);

        return ResponseEntity.ok(
                new ApiResponse(true, "Doctor deleted successfully")
        );
    }
}