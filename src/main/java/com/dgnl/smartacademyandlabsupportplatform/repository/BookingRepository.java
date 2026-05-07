package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.MentoringSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<MentoringSession,Long> {
}
