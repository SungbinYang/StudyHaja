package com.studyhaja.modules.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName : com.studyhaja.repository.tag
 * fileName : TagRepository
 * author : rovert
 * date : 2022/03/10
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/10       rovert         최초 생성
 */

@Transactional(readOnly = true)
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByTitle(String title);
}
