package com.hospital.management.service;

import com.hospital.management.model.Patient;
import java.util.List;

public interface PatientService {

    Patient createPatient(Patient patient, String email);   // Admin flow


    List<Patient> getAllPatients();

    Patient getPatientById(Long id);

    Patient updatePatient(Long id, Patient patient);

    void deletePatient(Long id);
}