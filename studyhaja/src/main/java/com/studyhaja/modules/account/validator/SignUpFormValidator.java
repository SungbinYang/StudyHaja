package com.studyhaja.modules.account.validator;

import com.studyhaja.modules.account.form.SignUpForm;
import com.studyhaja.modules.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * packageName : com.studyhaja.domain.account
 * fileName : SignUpFormValidator
 * author : rovert
 * date : 2022/03/03
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/03       rovert         최초 생성
 */

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) { // 검사하는 타입
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO: email, nickname 중복 여부 확인
        SignUpForm signUpForm = (SignUpForm) target;

        if (accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[] {
                    signUpForm.getEmail()
            }, "이미 사용중인 이메일입니다.");
        }

        if (accountRepository.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[] {
                    signUpForm.getNickname()
            }, "이미 사용중인 닉네임입니다.");
        }
    }
}
