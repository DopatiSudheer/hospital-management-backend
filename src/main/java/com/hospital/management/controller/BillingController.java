package com.hospital.management.controller;

import com.hospital.management.dto.ApiResponse;
import com.hospital.management.dto.BillingRequest;
import com.hospital.management.model.Billing;
import com.hospital.management.model.Patient;
import com.hospital.management.model.Appointment;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billings")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping
    public ResponseEntity<ApiResponse> createBilling(@RequestBody BillingRequest request) {
        try {
            // Validate required fields
            if (request.getBillDate() == null ||
                request.getConsultationFee() == null ||
                request.getTreatmentFee() == null ||
                request.getMedicineFee() == null ||
                request.getPaymentMode() == null || request.getPaymentMode().trim().isEmpty() ||
                request.getPatientId() == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Bill date, fees, payment mode, and patient ID are required"));
            }

            // Validate payment mode
            if (!request.getPaymentMode().equals("CASH") && 
                !request.getPaymentMode().equals("CARD") && 
                !request.getPaymentMode().equals("UPI")) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Invalid payment mode. Must be CASH, CARD, or UPI"));
            }

            // Validate payment status
            String paymentStatus = request.getPaymentStatus();
            if (paymentStatus == null || paymentStatus.trim().isEmpty()) {
                paymentStatus = "PENDING";
            } else if (!paymentStatus.equals("PAID") && !paymentStatus.equals("PENDING")) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Invalid payment status. Must be PAID or PENDING"));
            }

            // Validate patient exists
            Patient patient = patientRepository.findById(request.getPatientId())
                    .orElse(null);
            if (patient == null || !patient.getActive()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Patient not found or inactive"));
            }

            // Validate appointment if provided
            Appointment appointment = null;
            if (request.getAppointmentId() != null) {
                appointment = appointmentRepository.findById(request.getAppointmentId())
                        .orElse(null);
                if (appointment == null || !appointment.getActive()) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Appointment not found or inactive"));
                }
            }

            // Create billing
            Billing billing = new Billing();
            billing.setBillDate(request.getBillDate());
            billing.setConsultationFee(request.getConsultationFee());
            billing.setTreatmentFee(request.getTreatmentFee());
            billing.setMedicineFee(request.getMedicineFee());
            billing.setPaymentMode(request.getPaymentMode());
            billing.setPaymentStatus(paymentStatus);
            billing.setPatient(patient);
            billing.setAppointment(appointment);

            Billing createdBilling = billingService.createBilling(billing);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Billing record created successfully", createdBilling));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to create billing: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllBillings() {
        try {
            List<Billing> billings = billingService.getAllBillings();
            return ResponseEntity.ok(new ApiResponse(true, "Billings retrieved successfully", billings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to retrieve billings: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBillingById(@PathVariable Long id) {
        try {
            Billing billing = billingService.getBillingById(id);
            
            if (billing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Billing not found"));
            }
            
            return ResponseEntity.ok(new ApiResponse(true, "Billing retrieved successfully", billing));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to retrieve billing: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBilling(@PathVariable Long id, @RequestBody BillingRequest request) {
        try {
            // Validate required fields
            if (request.getBillDate() == null ||
                request.getConsultationFee() == null ||
                request.getTreatmentFee() == null ||
                request.getMedicineFee() == null ||
                request.getPaymentMode() == null || request.getPaymentMode().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Bill date, fees, and payment mode are required"));
            }

            // Validate payment mode
            if (!request.getPaymentMode().equals("CASH") && 
                !request.getPaymentMode().equals("CARD") && 
                !request.getPaymentMode().equals("UPI")) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Invalid payment mode. Must be CASH, CARD, or UPI"));
            }

            // Validate payment status if provided
            String paymentStatus = request.getPaymentStatus();
            if (paymentStatus != null && !paymentStatus.trim().isEmpty() &&
                !paymentStatus.equals("PAID") && !paymentStatus.equals("PENDING")) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Invalid payment status. Must be PAID or PENDING"));
            }

            // Get existing billing
            Billing existingBilling = billingService.getBillingById(id);
            if (existingBilling == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Billing not found"));
            }

            // Update billing
            Billing billing = new Billing();
            billing.setBillDate(request.getBillDate());
            billing.setConsultationFee(request.getConsultationFee());
            billing.setTreatmentFee(request.getTreatmentFee());
            billing.setMedicineFee(request.getMedicineFee());
            billing.setPaymentMode(request.getPaymentMode());
            billing.setPaymentStatus(paymentStatus != null && !paymentStatus.trim().isEmpty() 
                ? paymentStatus : existingBilling.getPaymentStatus());

            // Update patient if provided
            if (request.getPatientId() != null) {
                Patient patient = patientRepository.findById(request.getPatientId())
                        .orElse(null);
                if (patient == null || !patient.getActive()) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Patient not found or inactive"));
                }
                billing.setPatient(patient);
            } else {
                billing.setPatient(existingBilling.getPatient());
            }

            // Update appointment if provided
            if (request.getAppointmentId() != null) {
                Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                        .orElse(null);
                if (appointment == null || !appointment.getActive()) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Appointment not found or inactive"));
                }
                billing.setAppointment(appointment);
            } else {
                billing.setAppointment(existingBilling.getAppointment());
            }

            Billing updatedBilling = billingService.updateBilling(id, billing);
            
            if (updatedBilling == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Billing not found"));
            }
            
            return ResponseEntity.ok(new ApiResponse(true, "Billing updated successfully", updatedBilling));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to update billing: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBilling(@PathVariable Long id) {
        try {
            Billing billing = billingService.getBillingById(id);
            
            if (billing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Billing not found"));
            }
            
            billingService.deleteBilling(id);
            return ResponseEntity.ok(new ApiResponse(true, "Billing deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to delete billing: " + e.getMessage()));
        }
    }
}

