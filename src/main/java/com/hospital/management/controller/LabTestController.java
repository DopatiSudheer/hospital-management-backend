package com.hospital.management.controller;

import com.hospital.management.dto.ApiResponse;
import com.hospital.management.dto.LabTestRequest;
import com.hospital.management.model.LabTest;
import com.hospital.management.model.Patient;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.service.LabTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lab-tests")
public class LabTestController {

    @Autowired
    private LabTestService labTestService;

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping
    public ResponseEntity<ApiResponse> createLabTest(@RequestBody LabTestRequest request) {
        try {
            // Validate required fields
            if (request.getTestName() == null || request.getTestName().trim().isEmpty() ||
                request.getTestFee() == null ||
                request.getStatus() == null || request.getStatus().trim().isEmpty() ||
                request.getPatientId() == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Test name, test fee, status, and patient ID are required"));
            }

            // Validate test fee is non-negative
            if (request.getTestFee() < 0) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Test fee cannot be negative"));
            }

            // Validate status
            if (!isValidStatus(request.getStatus())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Invalid status. Must be PENDING, IN_PROGRESS, or COMPLETED"));
            }

            // Validate patient exists
            Optional<Patient> patientOptional = patientRepository.findById(request.getPatientId());
            if (patientOptional.isEmpty() || !patientOptional.get().getActive()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Patient not found or inactive"));
            }

            // Create lab test
            LabTest labTest = new LabTest();
            labTest.setTestName(request.getTestName().trim());
            labTest.setTestFee(request.getTestFee());
            labTest.setResult(request.getResult());
            labTest.setStatus(request.getStatus());
            labTest.setPatient(patientOptional.get());

            LabTest createdLabTest = labTestService.createLabTest(labTest);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Lab test created successfully", createdLabTest));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to create lab test: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllLabTests() {
        try {
            List<LabTest> labTests = labTestService.getAllLabTests();
            return ResponseEntity.ok(new ApiResponse(true, "Lab tests fetched successfully", labTests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to fetch lab tests: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLabTestById(@PathVariable Long id) {
        try {
            Optional<LabTest> labTest = labTestService.getLabTestById(id);
            if (labTest.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "Lab test fetched successfully", labTest.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Lab test not found or inactive"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to fetch lab test: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateLabTest(@PathVariable Long id, @RequestBody LabTestRequest request) {
        try {
            Optional<LabTest> existingLabTestOptional = labTestService.getLabTestById(id);
            if (existingLabTestOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Lab test not found or inactive"));
            }

            LabTest existingLabTest = existingLabTestOptional.get();

            // Update fields from request
            if (request.getTestName() != null && !request.getTestName().trim().isEmpty()) {
                existingLabTest.setTestName(request.getTestName().trim());
            }
            if (request.getTestFee() != null) {
                if (request.getTestFee() < 0) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Test fee cannot be negative"));
                }
                existingLabTest.setTestFee(request.getTestFee());
            }
            if (request.getResult() != null) {
                existingLabTest.setResult(request.getResult());
            }
            if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
                if (!isValidStatus(request.getStatus())) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Invalid status. Must be PENDING, IN_PROGRESS, or COMPLETED"));
                }
                existingLabTest.setStatus(request.getStatus());
            }

            // Update patient if provided
            if (request.getPatientId() != null) {
                Optional<Patient> patientOptional = patientRepository.findById(request.getPatientId());
                if (patientOptional.isEmpty() || !patientOptional.get().getActive()) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Patient not found or inactive"));
                }
                existingLabTest.setPatient(patientOptional.get());
            }

            LabTest updatedLabTest = labTestService.updateLabTest(id, existingLabTest);
            if (updatedLabTest != null) {
                return ResponseEntity.ok(new ApiResponse(true, "Lab test updated successfully", updatedLabTest));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to update lab test"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to update lab test: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteLabTest(@PathVariable Long id) {
        try {
            Optional<LabTest> labTestOptional = labTestService.getLabTestById(id);
            if (labTestOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Lab test not found or inactive"));
            }
            labTestService.deleteLabTest(id);
            return ResponseEntity.ok(new ApiResponse(true, "Lab test soft-deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to delete lab test: " + e.getMessage()));
        }
    }

    private boolean isValidStatus(String status) {
        return status.equals("PENDING") || 
               status.equals("IN_PROGRESS") || 
               status.equals("COMPLETED");
    }
}

