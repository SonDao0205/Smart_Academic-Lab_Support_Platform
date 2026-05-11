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
        // 1. Parse chuỗi từ input datetime-local
        // Định dạng của datetime-local là ISO_LOCAL_DATE_TIME (yyyy-MM-ddTHH:mm)
        LocalDateTime startDateTime = LocalDateTime.parse(dto.getStartDate());
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        LocalTime endTime = startTime.plusHours(1); // Mặc định mỗi ca tư vấn là 1 tiếng
        LocalTime beforeTime = startTime.minusHours(1);
        LocalTime afterTime = startTime.plusHours(1);

        // 2. NGHIỆP VỤ: Chặn đặt lịch trong quá khứ
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Không thể đặt lịch hẹn cho thời gian ở quá khứ!");
        }

        // 3. Tìm thông tin Lecturer từ bảng lecturers dựa trên userId truyền từ form
        Lecturer lecturer = lecturerRepository.findByUserId(dto.getLecturerId())
                .orElseThrow(() -> new RuntimeException("Giảng viên không tồn tại hoặc dữ liệu không hợp lệ!"));

        // 4. NGHIỆP VỤ: Chống xung đột (Cùng giảng viên không thể có 2 lịch trùng giờ)
        // Lưu ý: existsByLecturerId ở đây là ID của bảng LECTURERS
        boolean isConflict = sessionRepository.existsConflictWithBuffer(
                lecturer.getId(),
                date,
                "pending",
                beforeTime,
                afterTime
        );

        if (isConflict) {
            throw new RuntimeException("Giảng viên đã có lịch hẹn khác vào khung giờ này. Vui lòng chọn giờ khác!");
        }

        // 5. Thực hiện lưu vào Database
        MentoringSession session = new MentoringSession();
        session.setStudent(student);
        session.setLecturer(lecturer);
        session.setBookingDate(date);
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        // Status đã được gán mặc định là pending trong Entity của bạn

        sessionRepository.save(session);

        System.out.println("Đặt lịch thành công cho sinh viên: " + student.getFullName());
    }

    @Override
    @Transactional
    public void cancelBooking(Long sessionId, Long studentId) {
        MentoringSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn!"));

        // Kiểm tra quyền sở hữu (đảm bảo sinh viên chỉ hủy được lịch của chính mình)
        if (!session.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Bạn không có quyền hủy lịch này!");
        }

        // LOGIC: Kiểm tra thời gian (phải trước 24h)
        LocalDateTime appointmentTime = LocalDateTime.of(session.getBookingDate(), session.getStartTime());
        if (LocalDateTime.now().plusHours(24).isAfter(appointmentTime)) {
            throw new RuntimeException("Chỉ có thể hủy lịch trước thời gian bắt đầu ít nhất 24 giờ!");
        }

        session.setStatus(MentoringSessionEnum.rejected.toString());
        sessionRepository.save(session);
    }

    @Override
    public List<MentoringSession> getLecturerWaitingList(Long userId) {
        // Tìm lecturer tương ứng với user đang đăng nhập
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

        // Kiểm tra xem buổi này có phải của giảng viên đang đăng nhập không
        if (!session.getLecturer().getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thao tác trên lịch hẹn này!");
        }

        // Cập nhật trạng thái thành 'completed' (hoặc 'approved' tùy quy trình của bạn)
        // Ở đây tôi giả định nút "Xác nhận đã tư vấn" sẽ chuyển sang trạng thái hoàn thành
        session.setStatus(MentoringSessionEnum.approved.toString());
        sessionRepository.save(session);
    }

    @Override
    @Transactional
    public void rejectSession(Long sessionId, Long userId) {
        MentoringSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy buổi tư vấn!"));

        // Kiểm tra quyền sở hữu
        if (!session.getLecturer().getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thao tác!");
        }

        // Cập nhật trạng thái thành 'rejected'
        session.setStatus("rejected");
        sessionRepository.save(session);
    }


    @Override
    public MentoringSession getById(Long sessionId) {
        return sessionRepository.findById(sessionId).orElse(null);
    }
}