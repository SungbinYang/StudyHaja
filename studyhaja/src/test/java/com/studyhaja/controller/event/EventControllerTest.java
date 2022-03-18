package com.studyhaja.controller.event;

import com.studyhaja.common.WithAccount;
import com.studyhaja.controller.study.StudyControllerTest;
import com.studyhaja.domain.account.form.Account;
import com.studyhaja.domain.event.form.Event;
import com.studyhaja.domain.study.form.Study;
import com.studyhaja.enums.event.EventType;
import com.studyhaja.repository.enrollment.EnrollmentRepository;
import com.studyhaja.service.event.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * packageName : com.studyhaja.controller.event
 * fileName : EventControllerTest
 * author : rovert
 * date : 2022/03/17
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/17       rovert         최초 생성
 */

class EventControllerTest extends StudyControllerTest {

    @Autowired
    EventService eventService;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Test
    @WithAccount("sungbin")
    @DisplayName("선착순 모임에 참가 신청 - 자동 수락")
    void newEnrollment_to_FCFS_event_accepted() throws Exception {
        Account robert = createAccount("robert");
        Study study = createStudy("test-study", robert);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, robert);

        this.mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        Account sungbin = accountRepository.findByNickname("sungbin");
        isAccepted(sungbin, event);
    }

    private void isAccepted(Account account, Event event) {
        assertTrue(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
    }

    private Event createEvent(String eventTitle, EventType eventType, int limit, Study study, Account account) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setLimitOfEnrollments(limit);
        event.setTitle(eventTitle);
        event.setCreatedDateTime(LocalDateTime.now());

        event.setEndEnrollmentDateTime(LocalDateTime.now().plusDays(1));
        event.setStartDateTime(LocalDateTime.now().plusDays(1).plusHours(5));
        event.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(7));

        return eventService.createEvent(event, study, account);
    }
}