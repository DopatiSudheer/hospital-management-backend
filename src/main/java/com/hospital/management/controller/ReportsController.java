package com.hospital.management.controller;

import com.hospital.management.dto.ApiResponse;
import com.hospital.management.dto.MonthlyAppointmentCount;
import com.hospital.management.dto.MonthlyRevenue;
import com.hospital.management.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    @GetMapping("/monthly-appointments")
    public ResponseEntity<ApiResponse> getMonthlyAppointmentCounts() {
        try {
            List<MonthlyAppointmentCount> monthlyCounts = reportsService.getMonthlyAppointmentCounts();
            return ResponseEntity.ok(new ApiResponse(true, "Monthly appointment counts retrieved successfully", monthlyCounts));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(false, "Failed to retrieve monthly appointment counts: " + e.getMessage(), null));
        }
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<ApiResponse> getMonthlyRevenue() {
        try {
            List<MonthlyRevenue> monthlyRevenues = reportsService.getMonthlyRevenue();
            return ResponseEntity.ok(new ApiResponse(true, "Monthly revenue summary retrieved successfully", monthlyRevenues));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(false, "Failed to retrieve monthly revenue summary: " + e.getMessage(), null));
        }
    }
}

