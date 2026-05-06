package com.dgnl.smartacademyandlabsupportplatform.model.dto;

import com.dgnl.smartacademyandlabsupportplatform.validator.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatch
public class AuthRegisterDTO {
    @Email(message = "Email không hợp lệ!")
    @NotBlank(message = "Email không được bỏ trống!")
    private String email;
    @NotBlank(message = "Mật khẩu không được bỏ trống!")
    @Size(min = 6,max = 50,message = "Độ dài mật khẩu phải từ 6 - 50 kí tự!")
    private String password;
    @NotBlank(message = "Tên đầy đủ không được bỏ trống!")
    @Size(min = 5,max = 100,message = "Kí tự không hợp lệ!")
    private String fullName;
    @NotBlank(message = "Xác nhận mật khẩu không được bỏ trống!")
    private String confirmPassword;
    @Pattern(
            regexp = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$",
            message = "Số điện thoại không hợp lệ!"
    )
    @NotBlank(message = "Số điện thoại không được bỏ trống!")
    private String phone;
}
