package com.dgnl.smartacademyandlabsupportplatform.validator.impl;

import com.dgnl.smartacademyandlabsupportplatform.model.dto.AuthRegisterDTO;
import com.dgnl.smartacademyandlabsupportplatform.validator.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, AuthRegisterDTO> {
    @Override
    public boolean isValid(AuthRegisterDTO dto, ConstraintValidatorContext context) {
        boolean isValid = dto.getPassword() != null && dto.getPassword().equals(dto.getConfirmPassword());
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword").addConstraintViolation();
        }
        return isValid;
    }
}