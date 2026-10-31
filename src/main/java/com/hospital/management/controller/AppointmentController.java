package com.hospital.management.controller;

import com.hospital.management.dto.ApiResponse;
import com.hospital.management.dto.AppointmentRequest;
import com.hospital.management.model.Appointment;
import com.hospital.management.model.Patient;
import com.hospital.management.model.Doctor;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.service.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping
    public ResponseEntity<ApiResponse> createAppointment(@RequestBody AppointmentRequest request) {
        try {

            if (request.getAppointmentDate() == null ||
                request.getAppointmentTime() == null || request.getAppointmentTime().trim().isEmpty() ||
                request.getReason() == null || request.getReason().trim().isEmpty() ||
                request.getPatientId() == null || request.getDoctorId() == null) {

                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Appointment date, time, reason, patient ID and doctor ID are required"));
            }

            Patient patient = patientRepository.findById(request.getPatientId()).orElse(null);
            if (patient == null || !patient.getActive()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Patient not found or inactive"));
            }

            Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElse(null);
            if (doctor == null || !doctor.getActive()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Doctor not found or inactive"));
            }

            Appointment appointment = new Appointment();
            appointment.setAppointmentDate(request.getAppointmentDate());
            appointment.setAppointmentTime(request.getAppointmentTime());
            appointment.setReason(request.getReason());
            appointment.setStatus("SCHEDULED");
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);

            Appointment created = appointmentService.createAppointment(appointment);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Appointment created successfully", created));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            return ResponseEntity.ok(
                    new ApiResponse(true, "Appointments retrieved successfully", appointments)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to retrieve appointments: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAppointmentById(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);

            if (appointment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Appointment not found"));
            }

            return ResponseEntity.ok(
                    new ApiResponse(true, "Appointment retrieved successfully", appointment)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to retrieve appointment: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentRequest request) {

        try {

            Appointment existing = appointmentService.getAppointmentById(id);
            if (existing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Appointment not found"));
            }

            if (request.getAppointmentDate() == null ||
                request.getAppointmentTime() == null || request.getAppointmentTime().trim().isEmpty() ||
                request.getReason() == null || request.getReason().trim().isEmpty()) {

                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Appointment date, time and reason are required"));
            }

            Appointment updated = new Appointment();
            updated.setAppointmentDate(request.getAppointmentDate());
            updated.setAppointmentTime(request.getAppointmentTime());
            updated.setReason(request.getReason());
            updated.setStatus(existing.getStatus());

            if (request.getPatientId() != null) {
                Patient patient = patientRepository.findById(request.getPatientId()).orElse(null);
                if (patient == null || !patient.getActive()) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Patient not found or inactive"));
                }
                updated.setPatient(patient);
            } else {
                updated.setPatient(existing.getPatient());
            }

            if (request.getDoctorId() != null) {
                Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElse(null);
                if (doctor == null || !doctor.getActive()) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Doctor not found or inactive"));
                }
                updated.setDoctor(doctor);
            } else {
                updated.setDoctor(existing.getDoctor());
            }

            Appointment saved = appointmentService.updateAppointment(id, updated);

            return ResponseEntity.ok(
                    new ApiResponse(true, "Appointment updated successfully", saved)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelAppointment(@PathVariable Long id) {
        try {

            Appointment appointment = appointmentService.getAppointmentById(id);

            if (appointment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Appointment not found"));
            }

            appointmentService.cancelAppointment(id);

            return ResponseEntity.ok(
                    new ApiResponse(true, "Appointment cancelled successfully")
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAppointment(@PathVariable Long id) {
        try {

            Appointment appointment = appointmentService.getAppointmentById(id);

            if (appointment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Appointment not found"));
            }

            appointmentService.deleteAppointment(id);

            return ResponseEntity.ok(
                    new ApiResponse(true, "Appointment deleted successfully")
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getAppointmentsForUser(
            @RequestParam String role,
            @RequestParam Long userId) {

        List<Appointment> appointments =
                appointmentService.getAppointmentsForUser(role, userId);

        return ResponseEntity.ok(
                new ApiResponse(true, "Appointments retrieved successfully", appointments)
        );
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse> completeAppointment(@PathVariable Long id) {
        try {

            appointmentService.completeAppointment(id);

            return ResponseEntity.ok(
                    new ApiResponse(true, "Appointment marked as completed")
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/available-slots")
    public ResponseEntity<ApiResponse> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam String date) {

        try {

            List<String> slots =
                    appointmentService.getAvailableSlots(
                            doctorId,
                            LocalDate.parse(date)
                    );

            return ResponseEntity.ok(
                    new ApiResponse(true, "Available slots fetched successfully", slots)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}