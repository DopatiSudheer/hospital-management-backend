package com.hospital.management.repository;

import com.hospital.management.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Get all active appointments
    List<Appointment> findByActiveTrue();

    // Get appointment by id only if active
    Optional<Appointment> findByIdAndActiveTrue(Long id);

    // Count active appointments
    long countByActiveTrue();
    
    long countByDoctor_IdAndActiveTrue(Long doctorId);

    long countByPatient_IdAndActiveTrue(Long patientId);

    @Query("SELECT COUNT(DISTINCT a.patient.id) FROM Appointment a WHERE a.doctor.id = :doctorId AND a.active = true")
    Long countDistinctPatientsByDoctor(Long doctorId);

    // Check if doctor already has booking at same time (double booking prevention)
    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusAndActiveTrue(
            Long doctorId,
            LocalDate appointmentDate,
            String appointmentTime,
            String status
    );

    // Get already booked time slots for a doctor on a specific date
    @Query("SELECT a.appointmentTime FROM Appointment a " +
           "WHERE a.doctor.id = :doctorId " +
           "AND a.appointmentDate = :date " +
           "AND a.status = 'SCHEDULED' " +
           "AND a.active = true")
    List<String> findBookedSlots(Long doctorId, LocalDate date);

    // Monthly appointment count for reports
    @Query(value = "SELECT DATE_FORMAT(a.appointment_date, '%Y-%m') as month, COUNT(a.id) as count " +
           "FROM appointments a WHERE a.active = true " +
           "GROUP BY DATE_FORMAT(a.appointment_date, '%Y-%m') " +
           "ORDER BY DATE_FORMAT(a.appointment_date, '%Y-%m')",
           nativeQuery = true)
    List<Object[]> getMonthlyAppointmentCountsRaw();
 // Get appointments for specific doctor (by Id)
    List<Appointment> findByDoctor_IdAndActiveTrue(Long doctorId);

    // Get appointments for specific patient (by userId)
    List<Appointment> findByPatient_IdAndActiveTrue(Long patientId);
}