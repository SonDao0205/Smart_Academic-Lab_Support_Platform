package com.dgnl.smartacademyandlabsupportplatform.service.impl;

import com.dgnl.smartacademyandlabsupportplatform.exception.GetById;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.EquipmentDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Equipment;
import com.dgnl.smartacademyandlabsupportplatform.repository.EquipmentRepository;
import com.dgnl.smartacademyandlabsupportplatform.service.EquipmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }
    @Override
    public List<Equipment> getAll() {
        return equipmentRepository.findAll();
    }

    @Override
    public Equipment getById(long id) {
        if(!equipmentRepository.existsById(id)){
            throw new GetById("Thiết bị/tài liệu không hợp lệ!");
        }
        return equipmentRepository.getById(id);
    }

    @Override
    public Equipment save(EquipmentDTO equipmentDTO) {
        Equipment equipment;

        if (equipmentDTO.getId() != null && equipmentRepository.existsById((long) equipmentDTO.getId())) {
            equipment = equipmentRepository.findById((long) equipmentDTO.getId())
                    .orElseThrow(() -> new GetById("Không tìm thấy thiết bị để cập nhật!"));
        } else {
            equipment = new Equipment();
        }

        equipment.setName(equipmentDTO.getName());
        equipment.setDescription(equipmentDTO.getDescription());
        equipment.setQuantity(equipmentDTO.getQuantity());
        equipment.setLab(equipmentDTO.getLab());

        return equipmentRepository.save(equipment);
    }

    @Override
    public void deleteById(long id) {
        if(!equipmentRepository.existsById(id)){
            throw new GetById("Thiết bị/tài liệu không hợp lệ!");
        }
        equipmentRepository.deleteById(id);
    }
}
