package com.dgnl.smartacademyandlabsupportplatform.validator;

import com.dgnl.smartacademyandlabsupportplatform.validator.impl.PasswordMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatch {
    String message() default "Mật khẩu xác nhận không khớp!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}