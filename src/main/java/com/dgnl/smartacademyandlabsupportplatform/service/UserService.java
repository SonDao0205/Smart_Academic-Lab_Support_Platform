package com.dgnl.smartacademyandlabsupportplatform.service;

import com.dgnl.smartacademyandlabsupportplatform.model.dto.EquipmentDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.UserAccountDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.UserDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Equipment;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lecturer;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserService {
    List<UserAccountDTO> getAllWithDepartment();
    User getById(long id);
    void deleteById(long id);
    void save(UserDTO userDTO);
    Lecturer getLecturerByUserId(Long userId);
}
