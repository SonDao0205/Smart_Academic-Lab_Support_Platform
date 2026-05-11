package com.dgnl.smartacademyandlabsupportplatform.service;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.Borrowing;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.BorrowingDetail;

import java.util.List;

public interface BorrowingService {
    List<Borrowing> getPendingRequests();
    Borrowing getById(Long id);
    void approveDetail(Long detailId);
    void rejectDetail(Long detailId);
    List<BorrowingDetail> getDetailsByUserId(Long userId);
}
