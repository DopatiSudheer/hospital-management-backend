package com.hospital.management.service;

import com.hospital.management.model.Billing;
import com.hospital.management.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Override
    public Billing createBilling(Billing billing) {
        billing.setActive(true);
        // Calculate total amount
        Double total = calculateTotalAmount(
            billing.getConsultationFee(),
            billing.getTreatmentFee(),
            billing.getMedicineFee()
        );
        billing.setTotalAmount(total);
        return billingRepository.save(billing);
    }

    @Override
    public List<Billing> getAllBillings() {
        return billingRepository.findByActiveTrue();
    }

    @Override
    public Billing getBillingById(Long id) {
        return billingRepository.findByIdAndActiveTrue(id)
                .orElse(null);
    }

    @Override
    public Billing updateBilling(Long id, Billing billing) {
        Billing existingBilling = billingRepository.findByIdAndActiveTrue(id)
                .orElse(null);
        
        if (existingBilling == null) {
            return null;
        }
        
        existingBilling.setBillDate(billing.getBillDate());
        existingBilling.setConsultationFee(billing.getConsultationFee());
        existingBilling.setTreatmentFee(billing.getTreatmentFee());
        existingBilling.setMedicineFee(billing.getMedicineFee());
        existingBilling.setPaymentMode(billing.getPaymentMode());
        existingBilling.setPaymentStatus(billing.getPaymentStatus());
        
        // Recalculate total amount
        Double total = calculateTotalAmount(
            existingBilling.getConsultationFee(),
            existingBilling.getTreatmentFee(),
            existingBilling.getMedicineFee()
        );
        existingBilling.setTotalAmount(total);
        
        if (billing.getPatient() != null) {
            existingBilling.setPatient(billing.getPatient());
        }
        
        if (billing.getAppointment() != null) {
            existingBilling.setAppointment(billing.getAppointment());
        }
        
        return billingRepository.save(existingBilling);
    }

    @Override
    public void deleteBilling(Long id) {
        Billing billing = billingRepository.findByIdAndActiveTrue(id)
                .orElse(null);
        
        if (billing != null) {
            billing.setActive(false);
            billingRepository.save(billing);
        }
    }

    @Override
    public Double calculateTotalAmount(Double consultationFee, Double treatmentFee, Double medicineFee) {
        if (consultationFee == null) consultationFee = 0.0;
        if (treatmentFee == null) treatmentFee = 0.0;
        if (medicineFee == null) medicineFee = 0.0;
        return consultationFee + treatmentFee + medicineFee;
    }
}

