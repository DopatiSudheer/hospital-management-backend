package com.hospital.management.repository;

import com.hospital.management.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    
    List<Billing> findByActiveTrue();
    
    Optional<Billing> findByIdAndActiveTrue(Long id);
    
    long countByActiveTrue();
    
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Billing b WHERE b.active = true AND b.paymentStatus = 'PAID'")
    Double getTotalRevenue();
    
    @Query("SELECT COUNT(b) FROM Billing b WHERE b.active = true AND b.paymentStatus = 'PENDING'")
    Long countPendingPayments();
    
    @Query(value = "SELECT DATE_FORMAT(b.bill_date, '%Y-%m') as month, COALESCE(SUM(b.total_amount), 0) as totalRevenue " +
           "FROM billings b WHERE b.active = true AND b.payment_status = 'PAID' " +
           "GROUP BY DATE_FORMAT(b.bill_date, '%Y-%m') " +
           "ORDER BY DATE_FORMAT(b.bill_date, '%Y-%m')", nativeQuery = true)
    List<Object[]> getMonthlyRevenueRaw();
}

