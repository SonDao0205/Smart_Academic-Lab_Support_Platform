package com.dgnl.smartacademyandlabsupportplatform.service.impl;

import com.dgnl.smartacademyandlabsupportplatform.exception.GetById;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.EquipmentDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Equipment;
import com.dgnl.smartacademyandlabsupportplatform.repository.EquipmentRepository;
import com.dgnl.smartacademyandlabsupportplatform.service.EquipmentService;
import jakarta.transaction.Transactional;
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
        return equipmentRepository.findAllActive();
    }

    @Override
    public Equipment getById(long id) {
        return equipmentRepository.findActiveById(id)
                .orElseThrow(() -> new GetById("Thiết bị không tồn tại hoặc đã bị xóa!"));
    }

    @Override
    @Transactional
    public void save(EquipmentDTO equipmentDTO) {
        Equipment equipment;
        if (equipmentDTO.getId() != null) {
            equipment = equipmentRepository.findActiveById((long) equipmentDTO.getId())
                    .orElseThrow(() -> new GetById("Không tìm thấy thiết bị để cập nhật!"));
        } else {
            equipment = new Equipment();
            equipment.setDeleted(false);
        }

        equipment.setName(equipmentDTO.getName());
        equipment.setDescription(equipmentDTO.getDescription());
        equipment.setQuantity(equipmentDTO.getQuantity());
        equipment.setLab(equipmentDTO.getLab());

        equipmentRepository.save(equipment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        if (!equipmentRepository.existsById(id)) {
            throw new GetById("Thiết bị không tồn tại!");
        }
        equipmentRepository.softDeleteById(id);
    }

    @Override
    public List<Equipment> getByLab(Long id) {
        return equipmentRepository.getActiveEquipmentByLabId(id);
    }
}
