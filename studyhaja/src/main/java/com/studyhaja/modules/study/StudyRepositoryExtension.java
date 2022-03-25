package com.studyhaja.modules.study;

import com.studyhaja.modules.tag.Tag;
import com.studyhaja.modules.zone.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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

    List<Study> findByAccount(Set<Tag> tags, Set<Zone> zones);
}
