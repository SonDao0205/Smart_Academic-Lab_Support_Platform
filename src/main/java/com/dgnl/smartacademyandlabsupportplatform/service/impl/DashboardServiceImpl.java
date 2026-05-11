package com.dgnl.smartacademyandlabsupportplatform.service.impl;

import com.dgnl.smartacademyandlabsupportplatform.repository.BorrowingDetailRepository;
import com.dgnl.smartacademyandlabsupportplatform.repository.MentoringRepository;
import com.dgnl.smartacademyandlabsupportplatform.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final MentoringRepository mentoringRepository;
    private final BorrowingDetailRepository borrowingDetailRepository;

    public DashboardServiceImpl(MentoringRepository mentoringRepository, BorrowingDetailRepository borrowingDetailRepository) {
        this.mentoringRepository = mentoringRepository;
        this.borrowingDetailRepository = borrowingDetailRepository;
    }

    @Override
    public Map<String, Object> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("topLecturers", mentoringRepository.findTop5Lecturers());
        stats.put("equipmentStats", borrowingDetailRepository.getEquipmentDistribution());
        return stats;
    }
}
