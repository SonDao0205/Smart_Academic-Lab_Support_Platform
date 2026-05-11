package com.dgnl.smartacademyandlabsupportplatform.service.impl;

import com.dgnl.smartacademyandlabsupportplatform.model.MentoringSessionEnum;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.AcademyEvaluationDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.*;
import com.dgnl.smartacademyandlabsupportplatform.repository.*;
import com.dgnl.smartacademyandlabsupportplatform.service.AcademyEvaluationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademyEvaluationServiceImpl implements AcademyEvaluationService {

    private final AcademyEvaluationRepository evaluationRepository;
    private final MentoringRepository mentoringRepository;
    private final BorrowingRepository borrowingRepository;
    private final BorrowingDetailRepository detailRepository;
    private final EquipmentRepository equipmentRepository;

    public AcademyEvaluationServiceImpl(AcademyEvaluationRepository evaluationRepository, MentoringRepository mentoringRepository, BorrowingRepository borrowingRepository, BorrowingDetailRepository detailRepository, EquipmentRepository equipmentRepository) {
        this.evaluationRepository = evaluationRepository;
        this.mentoringRepository = mentoringRepository;
        this.borrowingRepository = borrowingRepository;
        this.detailRepository = detailRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public List<AcademyEvaluation> getAll() {
        return evaluationRepository.findAll();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void save(AcademyEvaluationDTO dto) {
        MentoringSession session = mentoringRepository.findById(dto.getMentoringSessionId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca tư vấn"));

        // 1. Kiểm tra số lượng tồn kho của TẤT CẢ thiết bị trước khi làm bất cứ việc gì
        if (dto.getSelectedEquipments() != null && !dto.getSelectedEquipments().isEmpty()) {
            for (AcademyEvaluationDTO.EquipmentSelection item : dto.getSelectedEquipments()) {
                Equipment equipment = equipmentRepository.findById(item.getEquipmentId())
                        .orElseThrow(() -> new RuntimeException("Thiết bị không tồn tại"));

                if (item.getQuantity() > equipment.getQuantity()) {
                    // Ném lỗi ngay tại đây, chưa có dữ liệu nào bị insert vào DB
                    throw new RuntimeException("Thiết bị " + equipment.getName() + " chỉ còn " + equipment.getQuantity() + " sản phẩm.");
                }
            }
        }

        // 2. Nếu kiểm tra qua hết mới thực hiện lưu
        session.setStatus(MentoringSessionEnum.approved.toString());
        mentoringRepository.save(session);

        AcademyEvaluation evaluation = new AcademyEvaluation();
        evaluation.setContent(dto.getContent());
        evaluation.setMentoringSession(session);
        evaluation.setStatus("FINISHED");
        AcademyEvaluation savedEval = evaluationRepository.save(evaluation);

        if (dto.getSelectedEquipments() != null && !dto.getSelectedEquipments().isEmpty()) {
            Borrowing borrowing = new Borrowing();
            borrowing.setLecturer(session.getLecturer());
            borrowing.setUser(session.getStudent());
            borrowing.setStatus("PENDING");
            // Nếu có trường evaluation_id trong Borrowing thì set vào đây
            // borrowing.setAcademyEvaluation(savedEval);

            Borrowing savedBorrowing = borrowingRepository.save(borrowing);

            for (AcademyEvaluationDTO.EquipmentSelection item : dto.getSelectedEquipments()) {
                Equipment equipment = equipmentRepository.findById(item.getEquipmentId()).get();
                BorrowingDetail detail = new BorrowingDetail();
                detail.setBorrowing(savedBorrowing);
                detail.setEquipment(equipment);
                detail.setQuantity(item.getQuantity());
                detail.setStatus("REQUESTED");
                detailRepository.save(detail);
            }
        }
    }

    @Override
    public AcademyEvaluation getById(Long id) {
        return evaluationRepository.findById(id).orElse(null);
    }

    @Override
    public AcademyEvaluation getByMentoringSessionId(Long sessionId) {
        return evaluationRepository.findByMentoringSessionId(sessionId).orElse(null);
    }
}
