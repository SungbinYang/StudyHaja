package com.studyhaja.modules.event;

import com.studyhaja.modules.account.Account;
import com.studyhaja.modules.event.event.EnrollmentAcceptedEvent;
import com.studyhaja.modules.event.event.EnrollmentRejectedEvent;
import com.studyhaja.modules.event.form.EventForm;
import com.studyhaja.modules.study.Study;
import com.studyhaja.modules.study.event.StudyUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
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

    private final ModelMapper modelMapper;

    private final EnrollmentRepository enrollmentRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public Event createEvent(Event event, Study study, Account account) {
        event.setCreatedBy(account);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setStudy(study);
        applicationEventPublisher.publishEvent(new StudyUpdateEvent(event.getStudy(), "'" + event.getTitle() + "' 모임을 만들었습니다."));

        return eventRepository.save(event);
    }

    public void updateEvent(Event event, EventForm eventForm) {
        modelMapper.map(eventForm, event);
        applicationEventPublisher.publishEvent(new StudyUpdateEvent(event.getStudy(), "'" + event.getTitle() + "' 모임 정보를 수정했으니 확인바랍니다."));

        event.acceptWaitingList();
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
        applicationEventPublisher.publishEvent(new StudyUpdateEvent(event.getStudy(), "'" + event.getTitle() + "' 모임을 취소했습니다."));
    }

    public void newEnrollment(Event event, Account account) {
        if (!enrollmentRepository.existsByEventAndAccount(event, account)) {
            Enrollment enrollment = new Enrollment();

            enrollment.setEnrolledAt(LocalDateTime.now());
            enrollment.setAccepted(event.isAbleToAcceptWaitingEnrollment());

            enrollment.setAccount(account);
            event.addEnrollment(enrollment);

            enrollmentRepository.save(enrollment);
        }
    }

    public void cancelEnrollment(Event event, Account account) {
        Enrollment enrollment = enrollmentRepository.findByEventAndAccount(event, account);

        if (!enrollment.isAttended()) {
            event.removeEnrollment(enrollment);
            enrollmentRepository.delete(enrollment);
            event.acceptNextWaitingEnrollment();
        }
    }

    public void acceptEnrollment(Event event, Enrollment enrollment) {
        event.accept(enrollment);
        applicationEventPublisher.publishEvent(new EnrollmentAcceptedEvent(enrollment));
    }

    public void rejectEnrollment(Event event, Enrollment enrollment) {
        event.reject(enrollment);
        applicationEventPublisher.publishEvent(new EnrollmentRejectedEvent(enrollment));
    }

    public void checkInEnrollment(Enrollment enrollment) {
        enrollment.setAttended(true);
    }

    public void cancelCheckInEnrollment(Enrollment enrollment) {
        enrollment.setAttended(false);
    }
}
