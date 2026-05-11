package com.dgnl.smartacademyandlabsupportplatform.service.impl;

import com.dgnl.smartacademyandlabsupportplatform.model.MentoringSessionEnum;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.BookingDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lecturer;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.MentoringSession;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.repository.LecturerRepository;
import com.dgnl.smartacademyandlabsupportplatform.repository.MentoringRepository;
import com.dgnl.smartacademyandlabsupportplatform.repository.UserRepository;
import com.dgnl.smartacademyandlabsupportplatform.service.BookingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final MentoringRepository sessionRepository;
    private final UserRepository userRepository;
    private final LecturerRepository lecturerRepository;

    public BookingServiceImpl(MentoringRepository sessionRepository, UserRepository userRepository, LecturerRepository lecturerRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public List<MentoringSession> getHistoryByStudent(Long studentId) {
        return sessionRepository.findAllByStudentIdOrderByBookingDateDesc(studentId);
    }

    @Override
    @Transactional
    public void createBooking(BookingDTO dto, User student) {
        LocalDate date = LocalDate.parse(dto.getBookingDate());
        LocalTime startTime = LocalTime.parse(dto.getStartTime());
        LocalTime endTime = startTime.plusHours(1);

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);

        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Không thể đặt lịch hẹn cho thời gian ở quá khứ!");
        }

        Lecturer lecturer = lecturerRepository.findByUserId(dto.getLecturerId())
                .orElseThrow(() -> new RuntimeException("Giảng viên không tồn tại!"));

        LocalTime beforeTime = startTime.minusMinutes(59);
        LocalTime afterTime = startTime.plusMinutes(59);

        boolean isConflict = sessionRepository.existsConflictWithBuffer(
                lecturer.getId(),
                date,
                "pending",
                beforeTime,
                afterTime
        );

        if (isConflict) {
            throw new RuntimeException("Giảng viên đã có lịch trong khung giờ này hoặc quá sát giờ này!");
        }

        MentoringSession session = new MentoringSession();
        session.setStudent(student);
        session.setLecturer(lecturer);
        session.setBookingDate(date);
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        session.setStatus("pending");

        sessionRepository.save(session);
    }

    @Override
    @Transactional
    public void cancelBooking(Long sessionId, Long studentId) {
        MentoringSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn!"));

        if (!session.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Bạn không có quyền hủy lịch này!");
        }

        LocalDateTime appointmentTime = LocalDateTime.of(session.getBookingDate(), session.getStartTime());
        if (LocalDateTime.now().plusHours(24).isAfter(appointmentTime)) {
            throw new RuntimeException("Chỉ có thể hủy lịch trước thời gian bắt đầu ít nhất 24 giờ!");
        }

        session.setStatus(MentoringSessionEnum.rejected.toString());
        sessionRepository.save(session);
    }

    @Override
    public List<MentoringSession> getLecturerWaitingList(Long userId) {
        Lecturer lecturer = lecturerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Giảng viên không tồn tại!"));

        return sessionRepository.findAllByLecturerIdAndStatusInOrderByBookingDateAscStartTimeAsc(
                lecturer.getId(), List.of(MentoringSessionEnum.pending.toString()));
    }

    @Override
    @Transactional
    public void completeSession(Long sessionId, Long userId) {
        MentoringSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy buổi tư vấn!"));

        if (!session.getLecturer().getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thao tác trên lịch hẹn này!");
        }

        session.setStatus(MentoringSessionEnum.approved.toString());
        sessionRepository.save(session);
    }

    @Override
    @Transactional
    public void rejectSession(Long sessionId, Long userId) {
        MentoringSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy buổi tư vấn!"));

        if (!session.getLecturer().getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thao tác!");
        }

        session.setStatus("rejected");
        sessionRepository.save(session);
    }


    @Override
    public MentoringSession getById(Long sessionId) {
        return sessionRepository.findById(sessionId).orElse(null);
    }
}