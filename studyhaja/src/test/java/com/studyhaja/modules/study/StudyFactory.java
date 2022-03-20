package com.studyhaja.modules.study;

import com.studyhaja.modules.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * packageName : com.studyhaja.modules.study
 * fileName : StudyFactory
 * author : rovert
 * date : 2022/03/20
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/20       rovert         최초 생성
 */

@Component
public class StudyFactory {

    @Autowired
    StudyService studyService;

    @Autowired
    StudyRepository studyRepository;

    public Study createStudy(String path, Account account) {
        Study study = new Study();
        study.setPath(path);
        studyService.createNewStudy(study, account);

        return study;
    }
}
