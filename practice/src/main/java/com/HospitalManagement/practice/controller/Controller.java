package com.HospitalManagement.practice.controller;

import com.HospitalManagement.practice.model.*;
import com.HospitalManagement.practice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
        private final DoctorRepo doctorRepo;
        private final DepartmentRepo departmentRepo;
        private final PatientRepo patientRepo;
        private final AppointmentRepo appointmentRepo;
        private final InsuranceRepo insuranceRepo;

        // ----- Doctor -----
        @PostMapping("/doctors")
        public Doctor createDoctor(@RequestBody Doctor doctor) {
            List<Department> fetchedDepartments = new ArrayList<>();
            for (Department d : doctor.getDepartments()) {
                Department dept = departmentRepo.findById(d.getId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                fetchedDepartments.add(dept);
            }
            doctor.setDepartments(fetchedDepartments);
            return doctorRepo.save(doctor);
        }

        @GetMapping("/doctors")
        public List<Doctor> getAllDoctors() {
            return doctorRepo.findAll();
        }

        // ----- Department -----
        @PostMapping("/departments")
        public Department createDepartment(@RequestBody Department dept) {
            return departmentRepo.save(dept);
        }

        @GetMapping("/departments")
        public List<Department> getAllDepartments() {
            return departmentRepo.findAll();
        }

        // ----- Patient -----
        @PostMapping("/patients")
        public Patient createPatient(@RequestBody Patient patient) {
            return patientRepo.save(patient);
        }

        @GetMapping("/patients")
        public List<Patient> getAllPatients() {
            return patientRepo.findAll();
        }

        // ----- Appointment -----
        @PostMapping("/appointments")
        public Appointment createAppointment(@RequestBody Appointment appt) {
            return appointmentRepo.save(appt);
        }

        @GetMapping("/appointments")
        public List<Appointment> getAllAppointments() {
            return appointmentRepo.findAll();
        }

        // ----- Insurance -----
        @PostMapping("/insurances")
        public Insurance createInsurance(@RequestBody Insurance insurance) {
            return insuranceRepo.save(insurance);
        }

        @GetMapping("/insurances")
        public List<Insurance> getAllInsurances() {
            return insuranceRepo.findAll();
        }

}
