package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.dto.EquipmentStatusDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.BorrowingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingDetailRepository extends JpaRepository<BorrowingDetail,Long> {
    List<BorrowingDetail> findAllByBorrowingUserIdOrderByIdDesc(Long userId);
    @Query(value = "SELECT status, SUM(quantity) as count " +
            "FROM borrowing_details " +
            "GROUP BY status", nativeQuery = true)
    List<EquipmentStatusDTO> getEquipmentDistribution();
}
