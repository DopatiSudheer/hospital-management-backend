package com.hospital.management.dto;

public class MonthlyAppointmentCount {
    
    private String month;
    private Long count;

    public MonthlyAppointmentCount() {
    }

    public MonthlyAppointmentCount(String month, Long count) {
        this.month = month;
        this.count = count;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

