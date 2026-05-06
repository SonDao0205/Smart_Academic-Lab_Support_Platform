package com.dgnl.smartacademyandlabsupportplatform.service.impl;

import com.dgnl.smartacademyandlabsupportplatform.exception.DataDuplicate;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.AuthLoginDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.AuthRegisterDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.repository.UserRepository;
import com.dgnl.smartacademyandlabsupportplatform.service.AuthService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    @Override
    public User login(AuthLoginDTO authLoginDTO) {
        User u =  userRepository.findByEmail(authLoginDTO.getEmail());
        if(u == null){
            return null;
        }
        if(!BCrypt.checkpw(authLoginDTO.getPassword(), u.getPassword())){
            return null;
        }
        return u;
    }

    @Override
    public User register(AuthRegisterDTO authRegisterDTO) {
        if(userRepository.findByEmail(authRegisterDTO.getEmail()) != null){
            throw new DataDuplicate("email","Email đã tồn tại!");
        }
        if(userRepository.findByPhone(authRegisterDTO.getPhone()) != null){
            throw new DataDuplicate("phone","Số điện thoại đã tồn tại!");
        }
        User user = new User();
        user.setFullName(authRegisterDTO.getFullName());
        String year = String.valueOf(java.time.Year.now().getValue());
        String uniqueSuffix = String.valueOf(System.currentTimeMillis()).substring(9);
        String generatedCode = "STU-" + year + "-" + uniqueSuffix;
        user.setUserCode(generatedCode);
        user.setEmail(authRegisterDTO.getEmail());
        user.setPhone(authRegisterDTO.getPhone());
        user.setPassword(hashPassword(authRegisterDTO.getPassword()));
        return userRepository.save(user);
    }
}
