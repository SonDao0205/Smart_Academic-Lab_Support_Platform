package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.MentoringSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MentoringRepository extends JpaRepository<MentoringSession, Long> {
    // Spring sẽ hiểu:
    // LecturerId -> lecturer.id
    // BookingDate -> bookingDate
    // StartTime -> startTime
    boolean existsByLecturerIdAndBookingDateAndStartTimeAndStatus(
            Long lecturerId,
            LocalDate bookingDate,
            LocalTime startTime,
            String status
    );
    List<MentoringSession> findAllByStudentIdOrderByBookingDateDesc(Long studentId);
    List<MentoringSession> findAllByLecturerIdAndStatusInOrderByBookingDateAscStartTimeAsc(
            Long lecturerId, List<String> statuses);


}
