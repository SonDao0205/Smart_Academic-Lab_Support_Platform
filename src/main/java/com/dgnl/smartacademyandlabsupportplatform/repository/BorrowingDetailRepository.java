package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.BorrowingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingDetailRepository extends JpaRepository<BorrowingDetail,Long> {
    List<BorrowingDetail> findAllByBorrowingUserIdOrderByIdDesc(Long userId);
}
