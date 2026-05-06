package com.dgnl.smartacademyandlabsupportplatform.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "labs")
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
