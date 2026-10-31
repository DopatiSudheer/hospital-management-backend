package com.hospital.management.controller;

import com.hospital.management.dto.ApiResponse;
import com.hospital.management.dto.PrescriptionRequest;
import com.hospital.management.model.Prescription;
import com.hospital.management.model.Patient;
import com.hospital.management.model.Doctor;
import com.hospital.management.model.Appointment;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping
    public ResponseEntity<ApiResponse> createPrescription(@RequestBody PrescriptionRequest request) {
        try {
            // Validate required fields
            if (request.getPrescriptionDate() == null ||
                request.getDiagnosis() == null || request.getDiagnosis().trim().isEmpty() ||
                request.getMedicines() == null || request.getMedicines().trim().isEmpty() ||
                request.getPatientId() == null ||
                request.getDoctorId() == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Prescription date, diagnosis, medicines, patient ID, and doctor ID are required"));
            }

            // Validate patient exists
            Optional<Patient> patientOptional = patientRepository.findById(request.getPatientId());
            if (patientOptional.isEmpty() || !patientOptional.get().getActive()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Patient not found or inactive"));
            }

            // Validate doctor exists
            Optional<Doctor> doctorOptional = doctorRepository.findById(request.getDoctorId());
            if (doctorOptional.isEmpty() || !doctorOptional.get().getActive()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Doctor not found or inactive"));
            }

            // Validate appointment if provided
            Appointment appointment = null;
            if (request.getAppointmentId() != null) {
                Optional<Appointment> appointmentOptional = appointmentRepository.findById(request.getAppointmentId());
                if (appointmentOptional.isEmpty() || !appointmentOptional.get().getActive()) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Appointment not found or inactive"));
                }
                appointment = appointmentOptional.get();
            }

            // Create prescription
            Prescription prescription = new Prescription();
            prescription.setPrescriptionDate(request.getPrescriptionDate());
            prescription.setDiagnosis(request.getDiagnosis());
            prescription.setMedicines(request.getMedicines());
            prescription.setNotes(request.getNotes());
            prescription.setPatient(patientOptional.get());
            prescription.setDoctor(doctorOptional.get());
            prescription.setAppointment(appointment);

            Prescription createdPrescription = prescriptionService.createPrescription(prescription);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Prescription created successfully", createdPrescription));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to create prescription: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPrescriptions() {
        try {
            List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
            return ResponseEntity.ok(new ApiResponse(true, "Prescriptions fetched successfully", prescriptions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to fetch prescriptions: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPrescriptionById(@PathVariable Long id) {
        try {
            Optional<Prescription> prescription = prescriptionService.getPrescriptionById(id);
            if (prescription.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "Prescription fetched successfully", prescription.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Prescription not found or inactive"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to fetch prescription: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePrescription(@PathVariable Long id, @RequestBody PrescriptionRequest request) {
        try {
            Optional<Prescription> existingPrescriptionOptional = prescriptionService.getPrescriptionById(id);
            if (existingPrescriptionOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Prescription not found or inactive"));
            }

            Prescription existingPrescription = existingPrescriptionOptional.get();

            // Update fields from request
            if (request.getPrescriptionDate() != null) {
                existingPrescription.setPrescriptionDate(request.getPrescriptionDate());
            }
            if (request.getDiagnosis() != null && !request.getDiagnosis().trim().isEmpty()) {
                existingPrescription.setDiagnosis(request.getDiagnosis());
            }
            if (request.getMedicines() != null && !request.getMedicines().trim().isEmpty()) {
                existingPrescription.setMedicines(request.getMedicines());
            }
            if (request.getNotes() != null) {
                existingPrescription.setNotes(request.getNotes());
            }

            // Update relationships if provided
            if (request.getPatientId() != null) {
                Optional<Patient> patientOptional = patientRepository.findById(request.getPatientId());
                if (patientOptional.isEmpty() || !patientOptional.get().getActive()) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Patient not found or inactive"));
                }
                existingPrescription.setPatient(patientOptional.get());
            }
            if (request.getDoctorId() != null) {
                Optional<Doctor> doctorOptional = doctorRepository.findById(request.getDoctorId());
                if (doctorOptional.isEmpty() || !doctorOptional.get().getActive()) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Doctor not found or inactive"));
                }
                existingPrescription.setDoctor(doctorOptional.get());
            }
            if (request.getAppointmentId() != null) {
                Optional<Appointment> appointmentOptional = appointmentRepository.findById(request.getAppointmentId());
                if (appointmentOptional.isEmpty() || !appointmentOptional.get().getActive()) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Appointment not found or inactive"));
                }
                existingPrescription.setAppointment(appointmentOptional.get());
            }

            Prescription updatedPrescription = prescriptionService.updatePrescription(id, existingPrescription);
            if (updatedPrescription != null) {
                return ResponseEntity.ok(new ApiResponse(true, "Prescription updated successfully", updatedPrescription));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to update prescription"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to update prescription: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePrescription(@PathVariable Long id) {
        try {
            Optional<Prescription> prescriptionOptional = prescriptionService.getPrescriptionById(id);
            if (prescriptionOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Prescription not found or inactive"));
            }
            prescriptionService.deletePrescription(id);
            return ResponseEntity.ok(new ApiResponse(true, "Prescription soft-deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to delete prescription: " + e.getMessage()));
        }
    }
}

