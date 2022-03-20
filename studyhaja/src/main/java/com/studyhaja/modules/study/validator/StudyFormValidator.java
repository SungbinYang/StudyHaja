package com.studyhaja.modules.study.validator;

import com.studyhaja.modules.study.form.StudyForm;
import com.studyhaja.modules.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * packageName : com.studyhaja.domain.study.validator
 * fileName : StudyFormValidator
 * author : rovert
 * date : 2022/03/12
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/12       rovert         최초 생성
 */

@Component
@RequiredArgsConstructor
public class StudyFormValidator implements Validator {

    private final StudyRepository studyRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return StudyForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StudyForm studyForm = (StudyForm) target;

        if (studyRepository.existsByPath(studyForm.getPath())) {
            errors.rejectValue("path", "wrong.path", "스터디 경로 값을 사용할 수 없습니다.");
        }
    }
}
