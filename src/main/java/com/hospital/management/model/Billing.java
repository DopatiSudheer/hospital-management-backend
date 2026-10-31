package com.hospital.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "billings")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate billDate;

    @Column(nullable = false)
    private Double consultationFee;

    @Column(nullable = false)
    private Double treatmentFee;

    @Column(nullable = false)
    private Double medicineFee;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String paymentMode;

    @Column(nullable = false)
    private String paymentStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Appointment appointment;

    @Column(nullable = false)
    private Boolean active;

    public Billing() {
        this.active = true;
        this.consultationFee = 0.0;
        this.treatmentFee = 0.0;
        this.medicineFee = 0.0;
        this.totalAmount = 0.0;
        this.paymentStatus = "PENDING";
    }

    public Billing(LocalDate billDate, Double consultationFee, Double treatmentFee, 
                   Double medicineFee, String paymentMode, String paymentStatus, 
                   Patient patient, Appointment appointment) {
        this.billDate = billDate;
        this.consultationFee = consultationFee;
        this.treatmentFee = treatmentFee;
        this.medicineFee = medicineFee;
        this.totalAmount = consultationFee + treatmentFee + medicineFee;
        this.paymentMode = paymentMode;
        this.paymentStatus = paymentStatus;
        this.patient = patient;
        this.appointment = appointment;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public Double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(Double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public Double getTreatmentFee() {
        return treatmentFee;
    }

    public void setTreatmentFee(Double treatmentFee) {
        this.treatmentFee = treatmentFee;
    }

    public Double getMedicineFee() {
        return medicineFee;
    }

    public void setMedicineFee(Double medicineFee) {
        this.medicineFee = medicineFee;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

