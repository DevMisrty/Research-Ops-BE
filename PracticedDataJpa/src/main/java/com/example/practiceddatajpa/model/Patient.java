package com.example.practiceddatajpa.model;

import com.example.practiceddatajpa.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Patient {

    @Id @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dob;
    private String email;
    private String bloodGroup;

    @CreationTimestamp
    private LocalDateTime createAt;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_insurance_id")
    @ToString.Exclude
    private Insurance insurance;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.DETACH} , orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "patient")
    @ToString.Exclude
    private List<Appointment> appointments;

}
