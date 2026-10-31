package com.hospital.management.dto;

public class DashboardSummary {
    
    private Long totalPatients;
    private Long totalDoctors;
    private Long totalAppointments;
    private Long totalBills;
    private Double totalRevenue;
    private Long pendingPayments;

    public DashboardSummary() {
    }

    public DashboardSummary(Long totalPatients, Long totalDoctors, Long totalAppointments, 
                           Long totalBills, Double totalRevenue, Long pendingPayments) {
        this.totalPatients = totalPatients;
        this.totalDoctors = totalDoctors;
        this.totalAppointments = totalAppointments;
        this.totalBills = totalBills;
        this.totalRevenue = totalRevenue;
        this.pendingPayments = pendingPayments;
    }

    public Long getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(Long totalPatients) {
        this.totalPatients = totalPatients;
    }

    public Long getTotalDoctors() {
        return totalDoctors;
    }

    public void setTotalDoctors(Long totalDoctors) {
        this.totalDoctors = totalDoctors;
    }

    public Long getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(Long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public Long getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(Long totalBills) {
        this.totalBills = totalBills;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getPendingPayments() {
        return pendingPayments;
    }

    public void setPendingPayments(Long pendingPayments) {
        this.pendingPayments = pendingPayments;
    }
}

