package com.studyhaja.modules.event.event;

import com.studyhaja.infra.config.AppProperties;
import com.studyhaja.infra.mail.EmailMessage;
import com.studyhaja.infra.mail.EmailService;
import com.studyhaja.modules.account.Account;
import com.studyhaja.modules.event.Enrollment;
import com.studyhaja.modules.event.Event;
import com.studyhaja.modules.notification.Notification;
import com.studyhaja.modules.notification.NotificationRepository;
import com.studyhaja.modules.notification.NotificationType;
import com.studyhaja.modules.study.Study;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

/**
 * packageName : com.studyhaja.modules.event.event
 * fileName : EnrollmentEventListener
 * author : rovert
 * date : 2022/03/22
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/22       rovert         최초 생성
 */

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class EnrollmentEventListener {

    private final NotificationRepository notificationRepository;

    private final AppProperties appProperties;

    private final TemplateEngine templateEngine;

    private final EmailService emailService;

    @EventListener
    public void handleEnrollmentEvent(EnrollmentEvent enrollmentEvent) {
        Enrollment enrollment = enrollmentEvent.getEnrollment();
        Account account = enrollment.getAccount();
        Event event = enrollment.getEvent();
        Study study = event.getStudy();

        if (account.isStudyEnrollmentResultByEmail()) {
            sendEmail(enrollmentEvent, account, event, study);
        }

        if (account.isStudyEnrollmentResultByWeb()) {
            createNotification(enrollmentEvent, account, event, study);
        }
    }

    private void sendEmail(EnrollmentEvent enrollmentEvent, Account account, Event event, Study study) {
        Context context = new Context();

        context.setVariable("nickname", account.getNickname());
        context.setVariable("link", "/study/" + study.getEncodePath() + "/events/" + event.getId());
        context.setVariable("linkName", study.getTitle());
        context.setVariable("message", enrollmentEvent.getMessage());
        context.setVariable("host", appProperties.getHost());

        String message = templateEngine.process("mail/simple-link", context);
        EmailMessage emailMessage = EmailMessage.builder()
                .subject("스터디하자, '" + event.getTitle() + "' 모임 참가 신청 결과입니다.")
                .to(account.getEmail())
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }

    private void createNotification(EnrollmentEvent enrollmentEvent, Account account, Event event, Study study) {
        Notification notification = new Notification();

        notification.setTitle(study.getTitle() + " / " + event.getTitle());
        notification.setLink("/study/" + study.getEncodePath() + "/events/" + event.getId());
        notification.setChecked(false);
        notification.setCreatedDateTime(LocalDateTime.now());
        notification.setMessage(enrollmentEvent.getMessage());
        notification.setAccount(account);
        notification.setNotificationType(NotificationType.EVENT_ENROLLMENT);

        notificationRepository.save(notification);
    }
}
