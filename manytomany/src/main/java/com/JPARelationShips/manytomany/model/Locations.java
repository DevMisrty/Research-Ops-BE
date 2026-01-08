package com.JPARelationShips.manytomany.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Locations {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String longitude;
    private String latitude;
}
