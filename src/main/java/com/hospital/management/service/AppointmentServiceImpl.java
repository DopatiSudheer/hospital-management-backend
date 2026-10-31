package com.hospital.management.service;

import com.hospital.management.model.Appointment;
import com.hospital.management.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    private final LocalTime START_TIME = LocalTime.of(9, 0);
    private final LocalTime END_TIME = LocalTime.of(18, 0);
    private final int SLOT_DURATION_MINUTES = 30;

    private void validateSlotTime(String appointmentTime) {

        LocalTime time = LocalTime.parse(appointmentTime);

        if (time.isBefore(START_TIME) || !time.isBefore(END_TIME)) {
            throw new RuntimeException("Appointments allowed only between 09:00 and 18:00");
        }

        if (time.getMinute() % SLOT_DURATION_MINUTES != 0) {
            throw new RuntimeException("Appointments must be in 30 minute slots");
        }
    }

    // =====================================================
    // CREATE
    // =====================================================
    @Override
    public Appointment createAppointment(Appointment appointment) {

        validateSlotTime(appointment.getAppointmentTime());

        boolean exists = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusAndActiveTrue(
                        appointment.getDoctor().getId(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime(),
                        "SCHEDULED"
                );

        if (exists) {
            throw new RuntimeException("Selected time slot already booked");
        }

        appointment.setActive(true);
        appointment.setStatus("SCHEDULED");

        return appointmentRepository.save(appointment);
    }

    // =====================================================
    // 🔥 ROLE BASED FETCH
    // =====================================================
    @Override
    public List<Appointment> getAppointmentsForUser(String role, Long userId) {

        if ("ADMIN".equals(role)) {
            return appointmentRepository.findByActiveTrue();
        }

        if ("DOCTOR".equals(role)) {
            return appointmentRepository.findByDoctor_IdAndActiveTrue(userId);
        }

        if ("PATIENT".equals(role)) {
            return appointmentRepository.findByPatient_IdAndActiveTrue(userId);
        }

        return new ArrayList<>();
    }

    // =====================================================
    // NORMAL GET ALL (still kept)
    // =====================================================
    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findByActiveTrue();
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findByIdAndActiveTrue(id)
                .orElse(null);
    }
    
    @Override
    public void completeAppointment(Long id) {

        Appointment appointment = appointmentRepository
                .findByIdAndActiveTrue(id)
                .orElse(null);

        if (appointment == null) {
            throw new RuntimeException("Appointment not found");
        }

        appointment.setStatus("COMPLETED");
        appointmentRepository.save(appointment);
    }

    // =====================================================
    // UPDATE
    // =====================================================
    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) {

        Appointment existing = appointmentRepository
                .findByIdAndActiveTrue(id)
                .orElse(null);

        if (existing == null) {
            return null;
        }

        validateSlotTime(appointment.getAppointmentTime());

        boolean exists = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusAndActiveTrue(
                        existing.getDoctor().getId(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime(),
                        "SCHEDULED"
                );

        if (exists &&
           (!existing.getAppointmentDate().equals(appointment.getAppointmentDate()) ||
            !existing.getAppointmentTime().equals(appointment.getAppointmentTime()))) {

            throw new RuntimeException("Selected time slot already booked");
        }

        existing.setAppointmentDate(appointment.getAppointmentDate());
        existing.setAppointmentTime(appointment.getAppointmentTime());
        existing.setReason(appointment.getReason());

        return appointmentRepository.save(existing);
    }

    // =====================================================
    // DELETE
    // =====================================================
    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository
                .findByIdAndActiveTrue(id)
                .orElse(null);

        if (appointment != null) {
            appointment.setActive(false);
            appointmentRepository.save(appointment);
        }
    }

    // =====================================================
    // CANCEL
    // =====================================================
    @Override
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository
                .findByIdAndActiveTrue(id)
                .orElse(null);

        if (appointment != null) {
            appointment.setStatus("CANCELLED");
            appointmentRepository.save(appointment);
        }
    }

    // =====================================================
    // AVAILABLE SLOTS
    // =====================================================
    @Override
    public List<String> getAvailableSlots(Long doctorId, LocalDate date) {

        List<String> bookedSlots =
                appointmentRepository.findBookedSlots(doctorId, date);

        List<String> allSlots = new ArrayList<>();

        LocalTime current = START_TIME;

        while (current.isBefore(END_TIME)) {
            allSlots.add(current.toString());
            current = current.plusMinutes(SLOT_DURATION_MINUTES);
        }

        allSlots.removeAll(bookedSlots);

        return allSlots;
    }
}