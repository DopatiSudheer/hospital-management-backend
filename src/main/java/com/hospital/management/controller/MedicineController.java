package com.hospital.management.controller;

import com.hospital.management.dto.ApiResponse;
import com.hospital.management.dto.MedicineRequest;
import com.hospital.management.model.Medicine;
import com.hospital.management.repository.MedicineRepository;
import com.hospital.management.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private MedicineRepository medicineRepository;

    @PostMapping
    public ResponseEntity<ApiResponse> createMedicine(@RequestBody MedicineRequest request) {
        try {
            // Validate required fields
            if (request.getMedicineName() == null || request.getMedicineName().trim().isEmpty() ||
                request.getPrice() == null ||
                request.getStock() == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Medicine name, price, and stock are required"));
            }

            // Validate price is non-negative
            if (request.getPrice() < 0) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Price cannot be negative"));
            }

            // Validate stock is non-negative
            if (request.getStock() < 0) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Stock cannot be negative"));
            }

            // Check if medicine name already exists
            Optional<Medicine> existingMedicine = medicineRepository.findByMedicineNameAndActiveTrue(
                request.getMedicineName().trim()
            );
            if (existingMedicine.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, "Medicine with this name already exists"));
            }

            // Create medicine
            Medicine medicine = new Medicine();
            medicine.setMedicineName(request.getMedicineName().trim());
            medicine.setPrice(request.getPrice());
            medicine.setStock(request.getStock());

            Medicine createdMedicine = medicineService.createMedicine(medicine);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Medicine created successfully", createdMedicine));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to create medicine: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllMedicines() {
        try {
            List<Medicine> medicines = medicineService.getAllMedicines();
            return ResponseEntity.ok(new ApiResponse(true, "Medicines fetched successfully", medicines));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to fetch medicines: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMedicineById(@PathVariable Long id) {
        try {
            Optional<Medicine> medicine = medicineService.getMedicineById(id);
            if (medicine.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "Medicine fetched successfully", medicine.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Medicine not found or inactive"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to fetch medicine: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateMedicine(@PathVariable Long id, @RequestBody MedicineRequest request) {
        try {
            Optional<Medicine> existingMedicineOptional = medicineService.getMedicineById(id);
            if (existingMedicineOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Medicine not found or inactive"));
            }

            Medicine existingMedicine = existingMedicineOptional.get();

            // Update fields from request
            if (request.getMedicineName() != null && !request.getMedicineName().trim().isEmpty()) {
                // Check if new name conflicts with another medicine
                Optional<Medicine> medicineWithSameName = medicineRepository.findByMedicineNameAndActiveTrue(
                    request.getMedicineName().trim()
                );
                if (medicineWithSameName.isPresent() && !medicineWithSameName.get().getId().equals(id)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse(false, "Medicine with this name already exists"));
                }
                existingMedicine.setMedicineName(request.getMedicineName().trim());
            }

            if (request.getPrice() != null) {
                if (request.getPrice() < 0) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Price cannot be negative"));
                }
                existingMedicine.setPrice(request.getPrice());
            }

            if (request.getStock() != null) {
                if (request.getStock() < 0) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Stock cannot be negative"));
                }
                existingMedicine.setStock(request.getStock());
            }

            Medicine updatedMedicine = medicineService.updateMedicine(id, existingMedicine);
            if (updatedMedicine != null) {
                return ResponseEntity.ok(new ApiResponse(true, "Medicine updated successfully", updatedMedicine));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to update medicine"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to update medicine: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMedicine(@PathVariable Long id) {
        try {
            Optional<Medicine> medicineOptional = medicineService.getMedicineById(id);
            if (medicineOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Medicine not found or inactive"));
            }
            medicineService.deleteMedicine(id);
            return ResponseEntity.ok(new ApiResponse(true, "Medicine soft-deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to delete medicine: " + e.getMessage()));
        }
    }
}

