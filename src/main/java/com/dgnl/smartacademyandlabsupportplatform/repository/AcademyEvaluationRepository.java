package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.AcademyEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcademyEvaluationRepository extends JpaRepository<AcademyEvaluation, Long> {
    Optional<AcademyEvaluation> findByMentoringSessionId(Long sessionId);
}
