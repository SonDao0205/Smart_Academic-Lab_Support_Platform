package com.dgnl.smartacademyandlabsupportplatform.model.dto;


import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lab;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentDTO {
    private Long id;
    @NotBlank(message = "Tên thiết bị/tài liệu không được bỏ trống!")
    @Size(min = 3,max = 150,message = "Độ dài tên của thiết bị/tài liệu không hợp lệ!")
    private String name;
    @NotBlank(message = "Mô tả thiết bị/tài liệu không được bỏ trống!")
    private String description;
    @NotNull(message = "Số lượng không được bỏ trống!")
    @Min(value = 0, message = "Số lượng không hợp lệ!")
    private Integer quantity;
    @NotNull(message = "Danh mục không được bỏ trống!")
    private Lab lab;
}
