package com.dgnl.smartacademyandlabsupportplatform.service.impl;

import com.dgnl.smartacademyandlabsupportplatform.exception.DataDuplicate;
import com.dgnl.smartacademyandlabsupportplatform.exception.GetById;
import com.dgnl.smartacademyandlabsupportplatform.exception.MissingInput;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.UserAccountDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.UserDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Department;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lecturer;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.repository.DeparmentRepository;
import com.dgnl.smartacademyandlabsupportplatform.repository.LecturerRepository;
import com.dgnl.smartacademyandlabsupportplatform.repository.UserRepository;
import com.dgnl.smartacademyandlabsupportplatform.service.UserService;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LecturerRepository lecturerRepository;
    private final DeparmentRepository departmentRepository;

    public UserServiceImpl(UserRepository userRepository,
                           LecturerRepository lecturerRepository,
                           DeparmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.lecturerRepository = lecturerRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<UserAccountDTO> getAllWithDepartment() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserAccountDTO dto = new UserAccountDTO();
            dto.setId(user.getId());
            dto.setUserCode(user.getUserCode());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setRole(user.getRole());
            dto.setStatus(user.isStatus());

            if ("lecturer".equals(user.getRole())) {
                Lecturer l = lecturerRepository.findByUserId(user.getId()).orElse(null);
                if (l != null && l.getDepartment() != null) {
                    dto.setDepartmentName(l.getDepartment().getName());
                    dto.setDepartmentId(l.getDepartment().getId());
                } else {
                    dto.setDepartmentName("-");
                }
            } else {
                dto.setDepartmentName("-");
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public User getById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new GetById("Không tìm thấy người dùng"));
    }

    @Override
    public Lecturer getLecturerByUserId(Long userId) {
        return lecturerRepository.findByUserId(userId).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        lecturerRepository.findByUserId(id).ifPresent(lecturerRepository::delete);
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void save(UserDTO dto) {
        User user;

        User existingCode = userRepository.findByUserCode(dto.getUserCode());
        if (existingCode != null && (dto.getId() == null || !existingCode.getId().equals(dto.getId()))) {
            throw new DataDuplicate("userCode", "Mã số người dùng này đã tồn tại!");
        }

        User existingEmail = userRepository.findByEmail(dto.getEmail());
        if (existingEmail != null && (dto.getId() == null || !existingEmail.getId().equals(dto.getId()))) {
            throw new DataDuplicate("email", "Email này đã tồn tại trong hệ thống!");
        }

        User existingPhone = userRepository.findByPhone(dto.getPhone());
        if (existingPhone != null && (dto.getId() == null || !existingPhone.getId().equals(dto.getId()))) {
            throw new DataDuplicate("phone", "Số điện thoại này đã tồn tại!");
        }


        if (dto.getId() != null) {
            user = userRepository.findById(dto.getId()).orElseThrow(() -> new GetById("User không tồn tại"));
        } else {
            user = new User();
            user.setStatus(true);
            if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
                throw new MissingInput("password", "Mật khẩu bắt buộc đối với tài khoản mới!");
            }else{
                if(dto.getPassword().length() < 6 ||  dto.getPassword().length() > 50) {
                    throw new MissingInput("password", "Độ dài mật khẩu phải từ 6 - 50 kí tự!!");
                }
            }
        }

        user.setFullName(dto.getFullName());
        user.setUserCode(dto.getUserCode());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        }

        user = userRepository.save(user);

        if ("lecturer".equals(dto.getRole())) {
            Lecturer lecturer = lecturerRepository.findByUserId(user.getId()).orElse(new Lecturer());
            lecturer.setUser(user);
            if (dto.getDepartmentId() != null) {
                Department dept = departmentRepository.findById(dto.getDepartmentId()).orElse(null);
                lecturer.setDepartment(dept);
            }
            lecturerRepository.save(lecturer);
        } else {
            lecturerRepository.findByUserId(user.getId()).ifPresent(lecturerRepository::delete);
        }
    }
}