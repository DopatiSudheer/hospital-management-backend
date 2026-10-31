package com.hospital.management.service;

import com.hospital.management.model.Billing;

import java.util.List;

public interface BillingService {
    
    Billing createBilling(Billing billing);
    
    List<Billing> getAllBillings();
    
    Billing getBillingById(Long id);
    
    Billing updateBilling(Long id, Billing billing);
    
    void deleteBilling(Long id);
    
    Double calculateTotalAmount(Double consultationFee, Double treatmentFee, Double medicineFee);
}

