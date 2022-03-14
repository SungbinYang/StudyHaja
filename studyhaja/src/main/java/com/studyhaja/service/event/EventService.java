package com.studyhaja.service.event;

import com.studyhaja.domain.account.form.Account;
import com.studyhaja.domain.event.form.Event;
import com.studyhaja.domain.study.form.Study;
import com.studyhaja.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * packageName : com.studyhaja.service.event
 * fileName : EventService
 * author : rovert
 * date : 2022/03/14
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/14       rovert         최초 생성
 */

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(Event event, Study study, Account account) {
        event.setCreatedBy(account);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setStudy(study);

        return eventRepository.save(event);
    }
}
