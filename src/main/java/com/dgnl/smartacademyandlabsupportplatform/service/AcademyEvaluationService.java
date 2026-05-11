package com.dgnl.smartacademyandlabsupportplatform.service;

import com.dgnl.smartacademyandlabsupportplatform.model.dto.AcademyEvaluationDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.AcademyEvaluation;

import java.util.List;

public interface AcademyEvaluationService {
    List<AcademyEvaluation> getAll();
    AcademyEvaluation getById(Long id);
    void save(AcademyEvaluationDTO academyEvaluationDTO);
    AcademyEvaluation getByMentoringSessionId(Long sessionId);
}
