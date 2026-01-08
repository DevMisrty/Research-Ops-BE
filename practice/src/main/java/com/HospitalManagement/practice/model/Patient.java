package com.HospitalManagement.practice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Patient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Gender gender;

    @Column(nullable = false, updatable = false)
    private LocalDate dateofbirth;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String bloodGroup;

    @OneToOne()
    private Insurance insurance;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments ;

    private enum Gender {
        MALE , FEMALE
    }

}
