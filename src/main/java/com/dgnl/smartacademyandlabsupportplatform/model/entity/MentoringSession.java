package com.dgnl.smartacademyandlabsupportplatform.model.entity;

import com.dgnl.smartacademyandlabsupportplatform.model.MentoringSessionEnum;
import com.dgnl.smartacademyandlabsupportplatform.model.SessionStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    private LocalDate bookingDate; // Ngày đặt lịch
    private LocalTime startTime;   // Giờ bắt đầu
    private LocalTime endTime;     // Giờ kết thúc

    private String status = MentoringSessionEnum.pending.toString();

}