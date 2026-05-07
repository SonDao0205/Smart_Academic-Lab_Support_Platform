package com.dgnl.smartacademyandlabsupportplatform.model.entity;

import com.dgnl.smartacademyandlabsupportplatform.model.SessionStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentoring_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentoringSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private SessionStatusEnum status;


}