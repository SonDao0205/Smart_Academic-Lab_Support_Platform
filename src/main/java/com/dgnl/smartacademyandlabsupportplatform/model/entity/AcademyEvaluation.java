package com.dgnl.smartacademyandlabsupportplatform.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "academic_evaluations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademyEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "mentoring_session_id")
    private MentoringSession mentoringSession;
    private String status;
}
