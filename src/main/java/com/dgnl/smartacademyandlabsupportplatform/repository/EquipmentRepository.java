package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.Equipment;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> getEquipmentByLabName(String labName);
    Equipment getById(Long id);

    List<Equipment> getEquipmentByLabId(Long labId);
}
