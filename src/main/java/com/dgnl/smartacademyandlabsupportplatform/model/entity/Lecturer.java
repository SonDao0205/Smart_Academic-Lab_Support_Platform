package com.dgnl.smartacademyandlabsupportplatform.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "lecturers")
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL)
    private List<Department> departments;

}
