package com.hospital.management.service;

import com.hospital.management.model.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    Appointment createAppointment(Appointment appointment);

    List<Appointment> getAllAppointments();

    Appointment getAppointmentById(Long id);

    Appointment updateAppointment(Long id, Appointment appointment);

    void deleteAppointment(Long id);

    void cancelAppointment(Long id);
    
    void completeAppointment(Long id);

    List<String> getAvailableSlots(Long doctorId, LocalDate date);
    
    List<Appointment> getAppointmentsForUser(String role, Long userId);
}