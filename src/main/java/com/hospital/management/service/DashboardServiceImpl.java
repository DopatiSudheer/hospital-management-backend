package com.hospital.management.service;

import com.hospital.management.dto.DashboardSummary;
import com.hospital.management.model.Doctor;
import com.hospital.management.model.User;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BillingRepository billingRepository;
    
    

    @Override
    public DashboardSummary getDashboardSummary(String role, Long userId) {

        Long totalPatients = 0L;
        Long totalDoctors = 0L;
        Long totalAppointments = 0L;
        Long totalBills = 0L;
        Double totalRevenue = 0.0;
        Long pendingPayments = 0L;

        if ("ADMIN".equals(role)) {

            totalPatients = patientRepository.countByActiveTrue();
            totalDoctors = doctorRepository.countByActiveTrue();
            totalAppointments = appointmentRepository.countByActiveTrue();
            totalBills = billingRepository.countByActiveTrue();

            Double revenue = billingRepository.getTotalRevenue();
            totalRevenue = (revenue != null) ? revenue : 0.0;

            Long pending = billingRepository.countPendingPayments();
            pendingPayments = (pending != null) ? pending : 0L;
        }

        else if ("DOCTOR".equals(role)) {

            // Get logged-in user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Find doctor using email
            Doctor doctor = doctorRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            Long doctorId = doctor.getId();

            totalAppointments = appointmentRepository
                    .countByDoctor_IdAndActiveTrue(doctorId);

            totalPatients = appointmentRepository
                    .countDistinctPatientsByDoctor(doctorId);
        }

        else if ("PATIENT".equals(role)) {

            Long patientId = patientRepository
                    .findByUserId(userId)
                    .get()
                    .getId();

            totalAppointments = appointmentRepository
                    .countByPatient_IdAndActiveTrue(patientId);
        }

        return new DashboardSummary(
                totalPatients,
                totalDoctors,
                totalAppointments,
                totalBills,
                totalRevenue,
                pendingPayments
        );
    }
    }

