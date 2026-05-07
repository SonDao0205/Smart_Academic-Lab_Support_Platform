package com.dgnl.smartacademyandlabsupportplatform.service;

import com.dgnl.smartacademyandlabsupportplatform.model.dto.EquipmentDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Equipment;

import java.util.List;

public interface EquipmentService{
    List<Equipment> getAll();
    Equipment getById(long id);
    void deleteById(long id);
    Equipment save(EquipmentDTO equipment);

}
