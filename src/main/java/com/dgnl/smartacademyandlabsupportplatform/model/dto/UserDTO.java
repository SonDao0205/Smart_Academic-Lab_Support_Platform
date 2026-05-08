package com.dgnl.smartacademyandlabsupportplatform.model.dto;


import com.dgnl.smartacademyandlabsupportplatform.model.entity.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank(message = "Tên đầy đủ không được bỏ trống!")
    @Size(min = 5,max = 100,message = "Kí tự không hợp lệ!")
    private String fullName;
    @NotBlank(message = "Mã người dùng không được bỏ trống!")
    private String userCode;
    @Email(message = "Email không hợp lệ!")
    @NotBlank(message = "Email không được bỏ trống!")
    private String email;
    private String password;
    @Pattern(
            regexp = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$",
            message = "Số điện thoại không hợp lệ!"
    )
    @NotBlank(message = "Số điện thoại không được bỏ trống!")
    private String phone;
    private String role;
    private boolean status;
    private Long departmentId;
}
