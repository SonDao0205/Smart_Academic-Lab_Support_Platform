package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.dto.LecturerStatsDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.MentoringSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
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
    Page<MentoringSession> findAllByStudentIdOrderByBookingDateDesc(Long studentId, Pageable pageable);

    @Query(value = "SELECT u.full_name as fullName, u.user_code as userCode, COUNT(m.id) as sessionCount " +
            "FROM mentoring_sessions m " +
            "JOIN lecturers l ON m.lecturer_id = l.id " +
            "JOIN users u ON l.user_id = u.id " +
            "WHERE m.status = 'approved' " +
            "GROUP BY l.id, u.full_name, u.user_code " +
            "ORDER BY sessionCount DESC LIMIT 5", nativeQuery = true)
    List<LecturerStatsDTO> findTop5Lecturers();

}
