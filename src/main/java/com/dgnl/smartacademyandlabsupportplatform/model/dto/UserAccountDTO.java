package com.dgnl.smartacademyandlabsupportplatform.model.dto;

import lombok.Data;

@Data
public class UserAccountDTO {
    private Long id;
    private String userCode;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private Long departmentId;
    private String departmentName;
    private boolean status;
}