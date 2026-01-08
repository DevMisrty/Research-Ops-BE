package com.HospitalManagement.practice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

    @Entity
    @Data
    @NoArgsConstructor
    public class Insurance {

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String policyNumber;

        @Column(nullable = false)
        private LocalDateTime validUntil;

        @Column(updatable = false,nullable = false)
        @CreationTimestamp
        private LocalDateTime createdAt;

        @OneToOne(mappedBy = "insurance")
        private Patient patient;
    }
