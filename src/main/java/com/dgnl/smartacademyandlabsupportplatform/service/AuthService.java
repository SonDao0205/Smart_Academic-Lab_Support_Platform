package com.dgnl.smartacademyandlabsupportplatform.service;

import com.dgnl.smartacademyandlabsupportplatform.model.dto.AuthLoginDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.AuthRegisterDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;

public interface AuthService {
    String hashPassword(String password);
    User login(AuthLoginDTO authLoginDTO);
    User register(AuthRegisterDTO authRegisterDTO);
}
