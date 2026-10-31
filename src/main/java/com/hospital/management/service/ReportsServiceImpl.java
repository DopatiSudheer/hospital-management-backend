package com.hospital.management.service;

import com.hospital.management.dto.MonthlyAppointmentCount;
import com.hospital.management.dto.MonthlyRevenue;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportsServiceImpl implements ReportsService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BillingRepository billingRepository;

    @Override
    public List<MonthlyAppointmentCount> getMonthlyAppointmentCounts() {
        List<Object[]> results = appointmentRepository.getMonthlyAppointmentCountsRaw();
        List<MonthlyAppointmentCount> monthlyCounts = new ArrayList<>();
        
        for (Object[] result : results) {
            String month = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            monthlyCounts.add(new MonthlyAppointmentCount(month, count));
        }
        
        return monthlyCounts;
    }

    @Override
    public List<MonthlyRevenue> getMonthlyRevenue() {
        List<Object[]> results = billingRepository.getMonthlyRevenueRaw();
        List<MonthlyRevenue> monthlyRevenues = new ArrayList<>();
        
        for (Object[] result : results) {
            String month = (String) result[0];
            Double totalRevenue = ((Number) result[1]).doubleValue();
            monthlyRevenues.add(new MonthlyRevenue(month, totalRevenue));
        }
        
        return monthlyRevenues;
    }
}

