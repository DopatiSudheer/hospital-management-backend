package com.hospital.management.dto;

import java.time.LocalDate;

public class PrescriptionRequest {
    
    private LocalDate prescriptionDate;
    private String diagnosis;
    private String medicines;
    private String notes;
    private Long patientId;
    private Long doctorId;
    private Long appointmentId;

    public PrescriptionRequest() {
    }

    public PrescriptionRequest(LocalDate prescriptionDate, String diagnosis, String medicines, 
                              String notes, Long patientId, Long doctorId, Long appointmentId) {
        this.prescriptionDate = prescriptionDate;
        this.diagnosis = diagnosis;
        this.medicines = medicines;
        this.notes = notes;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
    }

    public LocalDate getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
}

