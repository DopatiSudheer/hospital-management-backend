package com.hospital.management.service;

import com.hospital.management.model.Doctor;
import java.util.List;

public interface DoctorService {

    Doctor createDoctor(Doctor doctor, String email);

    List<Doctor> getAllDoctors();

    Doctor getDoctorById(Long id);

    Doctor updateDoctor(Long id, Doctor doctor);

    void deleteDoctor(Long id);
}