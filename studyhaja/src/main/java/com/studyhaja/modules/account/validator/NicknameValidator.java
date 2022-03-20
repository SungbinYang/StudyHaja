package com.studyhaja.modules.account.validator;

import com.studyhaja.modules.account.Account;
import com.studyhaja.modules.account.form.NicknameForm;
import com.studyhaja.modules.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * packageName : com.studyhaja.domain.settings
 * fileName : NicknameValidator
 * author : rovert
 * date : 2022/03/09
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/09       rovert         최초 생성
 */

@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm) target;
        Account byNickname = accountRepository.findByNickname(nicknameForm.getNickname());

        if (byNickname != null) {
            errors.rejectValue("nickname", "wrong.value", "입력하신 닉네임을 사용할 수 없습니다.");
        }
    }
}
