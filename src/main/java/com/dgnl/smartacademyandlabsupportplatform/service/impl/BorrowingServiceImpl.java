package com.dgnl.smartacademyandlabsupportplatform.service.impl;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.Borrowing;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.BorrowingDetail;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Equipment;
import com.dgnl.smartacademyandlabsupportplatform.repository.BorrowingDetailRepository;
import com.dgnl.smartacademyandlabsupportplatform.repository.BorrowingRepository;
import com.dgnl.smartacademyandlabsupportplatform.repository.EquipmentRepository;
import com.dgnl.smartacademyandlabsupportplatform.service.BorrowingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowingServiceImpl implements BorrowingService {
    private final BorrowingRepository borrowingRepository;
    private final BorrowingDetailRepository detailRepository;
    private final EquipmentRepository equipmentRepository;

    public BorrowingServiceImpl(BorrowingRepository borrowingRepository, BorrowingDetailRepository detailRepository, EquipmentRepository equipmentRepository) {
        this.borrowingRepository = borrowingRepository;
        this.detailRepository = detailRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public List<Borrowing> getPendingRequests() {
        return borrowingRepository.findByStatus("PENDING");
    }

    @Override
    public Borrowing getById(Long id) {
        return borrowingRepository.findById(id).orElse(null);
    }


    @Override
    @Transactional
    public void approveDetail(Long detailId) {
        BorrowingDetail detail = detailRepository.findById(detailId).orElseThrow();
        Equipment equipment = detail.getEquipment();

        if (equipment.getQuantity() < detail.getQuantity()) {
            throw new RuntimeException("Số lượng trong kho không đủ!");
        }
        equipment.setQuantity(equipment.getQuantity() - detail.getQuantity());

        detail.setStatus("APPROVED");
        equipmentRepository.save(equipment);
        detailRepository.save(detail);

        checkAndUpdateBorrowingStatus(detail.getBorrowing());
    }

    @Override
    public void rejectDetail(Long detailId) {
        BorrowingDetail detail = detailRepository.findById(detailId).orElseThrow();
        detail.setStatus("REJECTED");
        detailRepository.save(detail);
        checkAndUpdateBorrowingStatus(detail.getBorrowing());
    }

    private void checkAndUpdateBorrowingStatus(Borrowing borrowing) {
        boolean hasPending = borrowing.getDetails().stream()
                .anyMatch(d -> d.getStatus().equals("REQUESTED"));
        if (!hasPending) {
            borrowing.setStatus("PROCESSED");
            borrowingRepository.save(borrowing);
        }
    }
    @Override
    public List<BorrowingDetail> getDetailsByUserId(Long userId) {
        return detailRepository.findAllByBorrowingUserIdOrderByIdDesc(userId);
    }
}
