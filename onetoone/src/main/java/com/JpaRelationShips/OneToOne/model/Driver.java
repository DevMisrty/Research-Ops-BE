package com.JpaRelationShips.OneToOne.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Driver {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;
}
