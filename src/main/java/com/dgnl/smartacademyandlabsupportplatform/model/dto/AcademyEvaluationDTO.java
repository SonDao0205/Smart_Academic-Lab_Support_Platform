package com.dgnl.smartacademyandlabsupportplatform.model.dto;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.Equipment;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lab;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class AcademyEvaluationDTO {
    private Long mentoringSessionId;
    @NotBlank(message = "Nội dung đánh giá không được bỏ trống!")
    private String content;
    private Long labId;
    private List<EquipmentSelection> selectedEquipments = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipmentSelection {
        private Long equipmentId;
        private Integer quantity;
    }
}