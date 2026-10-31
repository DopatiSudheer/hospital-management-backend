package com.hospital.management.dto;

import java.time.LocalDate;

public class MedicalRecordRequest {
    
    private LocalDate visitDate;
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private Long patientId;

    public MedicalRecordRequest() {
    }

    public MedicalRecordRequest(LocalDate visitDate, String symptoms, String diagnosis, 
                               String treatment, Long patientId) {
        this.visitDate = visitDate;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.patientId = patientId;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}

