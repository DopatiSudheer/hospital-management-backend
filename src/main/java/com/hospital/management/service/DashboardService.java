package com.hospital.management.service;

import com.hospital.management.dto.DashboardSummary;

public interface DashboardService {
    
	DashboardSummary getDashboardSummary(String role, Long userId);
}

