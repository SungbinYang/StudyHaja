package com.studyhaja.repository.event;

import com.studyhaja.domain.event.form.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName : com.studyhaja.repository.event
 * fileName : EventRepository
 * author : rovert
 * date : 2022/03/14
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/14       rovert         최초 생성
 */

@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Long> {
}
