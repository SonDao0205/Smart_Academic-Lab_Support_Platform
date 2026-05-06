package com.dgnl.smartacademyandlabsupportplatform.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthLoginDTO {
    @Email(message = "Email không hợp lệ!")
    @NotBlank(message = "Email không được bỏ trống!")
    private String email;
    @NotBlank(message = "Mật khẩu không được bỏ trống!")
    private String password;
}
