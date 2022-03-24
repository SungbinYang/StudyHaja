package com.studyhaja.modules.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName : com.studyhaja.modules.study
 * fileName : StudyRepositoryExtension
 * author : rovert
 * date : 2022/03/23
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/23       rovert         최초 생성
 */

@Transactional(readOnly = true)
public interface StudyRepositoryExtension {

    Page<Study> findByKeyword(String keyword, Pageable pageable);
}
