package com.studyhaja.domain.settings.validator;

import com.studyhaja.domain.settings.form.PasswordForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * packageName : com.studyhaja.domain.settings
 * fileName : PasswordFormValidator
 * author : rovert
 * date : 2022/03/08
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/08       rovert         최초 생성
 */

public class PasswordFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PasswordForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordForm passwordForm = (PasswordForm) target;

        if (!passwordForm.getNewPassword().equals(passwordForm.getNewPasswordConfirm())) {
            errors.rejectValue("newPassword", "wrong.value", "입력한 새 패스워드가 일치하지 않습니다.");
        }
    }
}
