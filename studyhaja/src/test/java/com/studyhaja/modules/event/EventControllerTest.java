package com.studyhaja.modules.event;

import com.studyhaja.infra.AbstractContainerBaseTest;
import com.studyhaja.infra.MockMvcTest;
import com.studyhaja.modules.account.Account;
import com.studyhaja.modules.account.AccountFactory;
import com.studyhaja.modules.account.AccountRepository;
import com.studyhaja.modules.account.WithAccount;
import com.studyhaja.modules.study.Study;
import com.studyhaja.modules.study.StudyFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

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

@MockMvcTest
class EventControllerTest extends AbstractContainerBaseTest {

    @Autowired
    EventService eventService;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountFactory accountFactory;

    @Autowired
    StudyFactory studyFactory;

    @Autowired
    AccountRepository accountRepository;

    @Test
    @WithAccount("sungbin")
    @DisplayName("선착순 모임에 참가 신청 - 자동 수락")
    void newEnrollment_to_FCFS_event_accepted() throws Exception {
        Account robert = accountFactory.createAccount("robert");
        Study study = studyFactory.createStudy("test-study", robert);
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
        Account robert = accountFactory.createAccount("robert");
        Study study = studyFactory.createStudy("test-study", robert);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, robert);

        Account jacob = accountFactory.createAccount("jacob");
        Account jack = accountFactory.createAccount("jack");

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
        Account robert = accountFactory.createAccount("robert");
        Account jacob = accountFactory.createAccount("jacob");
        Study study = studyFactory.createStudy("test-study", robert);
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
        Account robert = accountFactory.createAccount("robert");
        Account jacob = accountFactory.createAccount("jacob");
        Study study = studyFactory.createStudy("test-study", robert);
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
        Account robert = accountFactory.createAccount("robert");
        Study study = studyFactory.createStudy("test-study", robert);
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