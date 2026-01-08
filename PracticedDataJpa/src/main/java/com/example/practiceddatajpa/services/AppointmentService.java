package com.example.practiceddatajpa.services;

import com.example.practiceddatajpa.model.Appointment;
import com.example.practiceddatajpa.model.Doctor;
import com.example.practiceddatajpa.model.Patient;
import com.example.practiceddatajpa.repo.AppointmentRepository;
import com.example.practiceddatajpa.repo.DoctorRepository;
import com.example.practiceddatajpa.repo.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public void createNewAppointment(Appointment appointment, UUID doctorId, UUID patientId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("doctor not found"));
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("patient not found"));

        if (appointment.getId() == null) {
            throw new RuntimeException("appointment id not found");
        }

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointmentRepository.save(appointment);
    }
}
