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

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    @WithAccount("sungbin")
    @DisplayName("선착순 모임에 참가 신청 - 대기 중 (이미 인원이 꽉 차 있음)")
    void newEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account robert = createAccount("robert");
        Study study = createStudy("test-study", robert);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, robert);

        Account jacob = createAccount("jacob");
        Account jack = createAccount("jack");

        eventService.newEnrollment(event, jacob);
        eventService.newEnrollment(event, jack);

        this.mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        Account sungbin = accountRepository.findByNickname("sungbin");
        isNotAccepted(sungbin, event);

    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("참가 신청 확정자가 선착순 모임에 참가 신청을 취소하는 경우, 바로 다음 대기자를 자동으로 신청 확인한다.")
    void accepted_account_cancelEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account sungbin = accountRepository.findByNickname("sungbin");
        Account robert = createAccount("robert");
        Account jacob = createAccount("jacob");
        Study study = createStudy("test-study", robert);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, robert);

        eventService.newEnrollment(event, jacob);
        eventService.newEnrollment(event, sungbin);
        eventService.newEnrollment(event, robert);

        isAccepted(jacob, event);
        isAccepted(sungbin, event);
        isNotAccepted(robert, event);

        this.mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/disenroll")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        isAccepted(jacob, event);
        isAccepted(robert, event);

        assertNull(enrollmentRepository.findByEventAndAccount(event, sungbin));
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("참가 신청 비확정자가 선착순 모임에 참가 신청을 취소하는 경우, 기존 확정자를 그대로 유지하고 새로운 확정자는 없다.")
    void not_accepted_account_cancelEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account sungbin = accountRepository.findByNickname("sungbin");
        Account robert = createAccount("robert");
        Account jacob = createAccount("jacob");
        Study study = createStudy("test-study", robert);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, robert);

        eventService.newEnrollment(event, jacob);
        eventService.newEnrollment(event, robert);
        eventService.newEnrollment(event, sungbin);

        isAccepted(jacob, event);
        isAccepted(robert, event);
        isNotAccepted(sungbin, event);

        this.mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/disenroll")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        isAccepted(jacob, event);
        isAccepted(robert, event);

        assertNull(enrollmentRepository.findByEventAndAccount(event, sungbin));
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("관리자 확인 모임에 참가 신청 - 대기중")
    void newEnrollment_to_CONFIMATIVE_event_not_accepted() throws Exception {
        Account robert = createAccount("robert");
        Study study = createStudy("test-study", robert);
        Event event = createEvent("test-event", EventType.CONFIRMATIVE, 2, study, robert);

        this.mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        Account sungbin = accountRepository.findByNickname("sungbin");
        isNotAccepted(sungbin, event);
    }

    private void isAccepted(Account account, Event event) {
        assertTrue(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
    }

    private void isNotAccepted(Account account, Event event) {
        assertFalse(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
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