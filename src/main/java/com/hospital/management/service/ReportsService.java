package com.hospital.management.service;

import com.hospital.management.dto.MonthlyAppointmentCount;
import com.hospital.management.dto.MonthlyRevenue;

import java.util.List;

public interface ReportsService {
    
    List<MonthlyAppointmentCount> getMonthlyAppointmentCounts();
    
    List<MonthlyRevenue> getMonthlyRevenue();
}

