package com.hospital.management.service;

import com.hospital.management.model.Prescription;
import com.hospital.management.repository.PrescriptionRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public Prescription createPrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findByActiveTrue();
    }

    @Override
    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findByIdAndActiveTrue(id);
    }

    @Override
    public Prescription updatePrescription(Long id, Prescription prescriptionDetails) {
        return prescriptionRepository.findByIdAndActiveTrue(id).map(prescription -> {
            prescription.setPrescriptionDate(prescriptionDetails.getPrescriptionDate());
            prescription.setDiagnosis(prescriptionDetails.getDiagnosis());
            prescription.setMedicines(prescriptionDetails.getMedicines());
            prescription.setNotes(prescriptionDetails.getNotes());
            
            // Update relationships if provided
            if (prescriptionDetails.getPatient() != null && prescriptionDetails.getPatient().getId() != null) {
                patientRepository.findById(prescriptionDetails.getPatient().getId()).ifPresent(prescription::setPatient);
            }
            if (prescriptionDetails.getDoctor() != null && prescriptionDetails.getDoctor().getId() != null) {
                doctorRepository.findById(prescriptionDetails.getDoctor().getId()).ifPresent(prescription::setDoctor);
            }
            if (prescriptionDetails.getAppointment() != null && prescriptionDetails.getAppointment().getId() != null) {
                appointmentRepository.findById(prescriptionDetails.getAppointment().getId()).ifPresent(prescription::setAppointment);
            }

            return prescriptionRepository.save(prescription);
        }).orElse(null);
    }

    @Override
    public void deletePrescription(Long id) {
        prescriptionRepository.findByIdAndActiveTrue(id).ifPresent(prescription -> {
            prescription.setActive(false);
            prescriptionRepository.save(prescription);
        });
    }
}

