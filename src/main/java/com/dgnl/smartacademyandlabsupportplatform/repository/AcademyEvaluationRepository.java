package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.AcademyEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademyEvaluationRepository extends JpaRepository<AcademyEvaluation, Long> {
    Optional<AcademyEvaluation> findByMentoringSessionId(Long sessionId);
}
