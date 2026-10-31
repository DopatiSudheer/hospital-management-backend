package com.hospital.management.controller;

import com.hospital.management.dto.ApiResponse;
import com.hospital.management.dto.DashboardSummary;
import com.hospital.management.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse> getDashboardSummary(
            @RequestParam String role,
            @RequestParam Long userId) {

        try {

            DashboardSummary summary =
                    dashboardService.getDashboardSummary(role, userId);

            return ResponseEntity.ok(
                    new ApiResponse(true, "Dashboard summary retrieved successfully", summary)
            );

        } catch (Exception e) {

            return ResponseEntity.ok(
                    new ApiResponse(false, "Failed: " + e.getMessage(), null)
            );
        }
    }
}

