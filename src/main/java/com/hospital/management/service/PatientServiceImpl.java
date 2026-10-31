package com.hospital.management.service;

import com.hospital.management.model.Patient;
import com.hospital.management.model.User;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    // =========================================================
    // CREATE PATIENT
    // Handles BOTH:
    // 1️⃣ Self Profile Completion (User already exists)
    // 2️⃣ Admin Creation (User does NOT exist)
    // =========================================================
    @Override
    public Patient createPatient(Patient patient, String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        // =====================================================
        // 🟢 SELF PROFILE COMPLETION FLOW
        // =====================================================
        if (optionalUser.isPresent()) {

            User existingUser = optionalUser.get();

            // Check if patient already linked
            Optional<Patient> existingPatient = patientRepository.findByUserId(existingUser.getId());

            if (existingPatient.isPresent()) {
                throw new RuntimeException("Profile already completed.");
            }

            patient.setUserId(existingUser.getId());
            patient.setActive(true);

            // Since profile is completed, no need for first login
            existingUser.setFirstLogin(false);
            userRepository.save(existingUser);

            return patientRepository.save(patient);
        }

        // =====================================================
        // 🔵 ADMIN CREATES NEW PATIENT FLOW
        // =====================================================

        // Generate default password: name@last4digits
        String contactNumber = patient.getContactNumber();

        if (contactNumber.length() < 4) {
            throw new RuntimeException("Contact number must be at least 4 digits.");
        }

        String lastFourDigits = contactNumber.substring(contactNumber.length() - 4);

        // Remove title prefixes (Mr., Ms., Mrs., etc.) from name
        String cleanName = patient.getPatientName().trim()
                .replaceAll("^(Dr\\.?|Mr\\.?|Ms\\.?|Mrs\\.?)\\s*", "");

        String defaultPassword = cleanName + "@" + lastFourDigits;

        // Create new user
        User newUser = new User(
                patient.getPatientName(),
                email,
                defaultPassword,
                "PATIENT");

        // Force password change on first login
        newUser.setFirstLogin(true);

        userRepository.save(newUser);

        // Link patient with new user
        patient.setUserId(newUser.getId());
        patient.setActive(true);

        return patientRepository.save(patient);
    }

    // =========================================================
    // GET ALL PATIENTS
    // =========================================================
    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findByActiveTrue();
    }

    // =========================================================
    // GET PATIENT BY ID
    // =========================================================
    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findByIdAndActiveTrue(id)
                .orElse(null);
    }

    // =========================================================
    // UPDATE PATIENT
    // =========================================================
    @Override
    public Patient updatePatient(Long id, Patient patient) {

        Patient existingPatient = patientRepository.findByIdAndActiveTrue(id).orElse(null);

        if (existingPatient == null) {
            return null;
        }

        existingPatient.setPatientName(patient.getPatientName());
        existingPatient.setGender(patient.getGender());
        existingPatient.setAge(patient.getAge());
        existingPatient.setBloodGroup(patient.getBloodGroup());
        existingPatient.setContactNumber(patient.getContactNumber());
        existingPatient.setAddress(patient.getAddress());

        return patientRepository.save(existingPatient);
    }

    // =========================================================
    // DELETE PATIENT (Soft Delete)
    // =========================================================
    @Override
    public void deletePatient(Long id) {

        Patient patient = patientRepository.findByIdAndActiveTrue(id).orElse(null);

        if (patient != null) {
            patient.setActive(false);
            patientRepository.save(patient);
        }
    }
}