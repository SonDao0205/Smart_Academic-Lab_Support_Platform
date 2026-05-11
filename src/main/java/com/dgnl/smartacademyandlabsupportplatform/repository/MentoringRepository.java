package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.MentoringSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MentoringRepository extends JpaRepository<MentoringSession, Long> {
    @Query("SELECT COUNT(ms) > 0 FROM MentoringSession ms " +
            "WHERE ms.lecturer.id = :lecturerId " +
            "AND ms.bookingDate = :date " +
            "AND ms.status = :status " +
            "AND ms.startTime > :beforeTime " +
            "AND ms.startTime < :afterTime")
    boolean existsConflictWithBuffer(
            @Param("lecturerId") Long lecturerId,
            @Param("date") LocalDate date,
            @Param("status") String status,
            @Param("beforeTime") LocalTime beforeTime,
            @Param("afterTime") LocalTime afterTime
    );
    List<MentoringSession> findAllByStudentIdOrderByBookingDateDesc(Long studentId);
    List<MentoringSession> findAllByLecturerIdAndStatusInOrderByBookingDateAscStartTimeAsc(
            Long lecturerId, List<String> statuses);


}
