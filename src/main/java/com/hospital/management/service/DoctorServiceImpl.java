package com.hospital.management.service;

import com.hospital.management.model.Doctor;
import com.hospital.management.model.User;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    // ================= CREATE DOCTOR (Admin Flow) =================
    @Override
    public Doctor createDoctor(Doctor doctor, String email) {

        // 1️⃣ Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        // 2️⃣ Generate default password: name@last4digits
        String contactNumber = doctor.getContactNumber();
        String lastFourDigits = contactNumber.substring(contactNumber.length() - 4);
        String defaultPassword = doctor.getDoctorName().trim() + "@" + lastFourDigits;

        // 3️⃣ Create User
        User user = new User(
                doctor.getDoctorName().trim(),
                email,
                defaultPassword,
                "DOCTOR"
        );

        // 🔥 Force password change on first login
        user.setFirstLogin(true);
        user.setActive(true);

        userRepository.save(user);

        // 4️⃣ Save Doctor
        doctor.setEmail(email);
        doctor.setActive(true);

        return doctorRepository.save(doctor);
    }

    // ================= GET ALL DOCTORS =================
    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findByActiveTrue();
    }

    // ================= GET DOCTOR BY ID =================
    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findByIdAndActiveTrue(id)
                .orElse(null);
    }

    // ================= UPDATE DOCTOR =================
    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {

        Doctor existingDoctor = doctorRepository.findByIdAndActiveTrue(id)
                .orElse(null);

        if (existingDoctor == null) {
            return null;
        }

        existingDoctor.setDoctorName(doctor.getDoctorName());
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setQualification(doctor.getQualification());
        existingDoctor.setExperience(doctor.getExperience());
        existingDoctor.setContactNumber(doctor.getContactNumber());

        return doctorRepository.save(existingDoctor);
    }

    // ================= DELETE DOCTOR (Soft Delete) =================
    @Override
    public void deleteDoctor(Long id) {

        Doctor doctor = doctorRepository.findByIdAndActiveTrue(id)
                .orElse(null);

        if (doctor != null) {
            doctor.setActive(false);
            doctorRepository.save(doctor);
        }
    }
}