package com.dgnl.smartacademyandlabsupportplatform.service;

import com.dgnl.smartacademyandlabsupportplatform.model.dto.BookingDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Borrowing;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.MentoringSession;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;

import java.util.List;

public interface BookingService{
    void createBooking(BookingDTO dto, User student);
    List<MentoringSession> getHistoryByStudent(Long studentId);
    void cancelBooking(Long sessionId, Long studentId);
    List<MentoringSession> getLecturerWaitingList(Long userId);
    void completeSession(Long sessionId, Long userId);
    void rejectSession(Long sessionId, Long userId);
    MentoringSession getById(Long sessionId);
}
