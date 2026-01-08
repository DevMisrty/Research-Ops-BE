package com.example.practiceddatajpa.repo;

import com.example.practiceddatajpa.model.Appointment;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AppointmentRepository extends CrudRepository<Appointment, UUID> {
}