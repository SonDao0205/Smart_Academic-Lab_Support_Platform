package com.dgnl.smartacademyandlabsupportplatform.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingDTO {
    @NotNull(message = "Khoa/Ngành không được bỏ trống!")
    private Long departmentId;

    @NotNull(message = "Giảng viên không được bỏ trống!")
    private Long lecturerId;

    @NotBlank(message = "Vui lòng chọn ngày!")
    private String bookingDate; // YYYY-MM-DD

    @NotBlank(message = "Vui lòng chọn khung giờ!")
    private String startTime;   // HH:mm
}
