package com.studyhaja.controller.event;

import com.studyhaja.common.WithAccount;
import com.studyhaja.controller.study.StudyControllerTest;
import com.studyhaja.domain.account.form.Account;
import com.studyhaja.domain.study.form.Study;
import com.studyhaja.repository.enrollment.EnrollmentRepository;
import com.studyhaja.service.event.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

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
        Account sungbin = createAccount("sungbin");
        Study study = createStudy("test-study", sungbin);
    }
}