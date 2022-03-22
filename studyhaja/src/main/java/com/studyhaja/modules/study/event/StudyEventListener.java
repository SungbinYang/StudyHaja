package com.studyhaja.modules.study.event;

import com.studyhaja.infra.config.AppProperties;
import com.studyhaja.infra.mail.EmailMessage;
import com.studyhaja.infra.mail.EmailService;
import com.studyhaja.modules.account.Account;
import com.studyhaja.modules.account.AccountPredicates;
import com.studyhaja.modules.account.AccountRepository;
import com.studyhaja.modules.notification.Notification;
import com.studyhaja.modules.notification.NotificationRepository;
import com.studyhaja.modules.notification.NotificationType;
import com.studyhaja.modules.study.Study;
import com.studyhaja.modules.study.StudyRepository;
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
 * packageName : com.studyhaja.modules.study.event
 * fileName : StudyEventListener
 * author : rovert
 * date : 2022/03/21
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/21       rovert         최초 생성
 */

@Async
@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class StudyEventListener {

    private final StudyRepository studyRepository;

    private final AccountRepository accountRepository;

    private final EmailService emailService;

    private final TemplateEngine templateEngine;

    private final AppProperties appProperties;

    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleStudyCreatedEvent(StudyCreatedEvent studyCreatedEvent) {
        Study study = studyRepository.findStudyWithTagsAndZonesById(studyCreatedEvent.getStudy().getId());

        Iterable<Account> accounts = accountRepository.findAll(AccountPredicates.findByTagsAndZones(study.getTags(), study.getZones()));
        accounts.forEach(account -> {
            if (account.isStudyCreatedByEmail()) {
                sendStudyCreatedEmail(study, account);
            }

            if (account.isStudyCreatedByWeb()) {
                saveStudyCreatedNotification(study, account);
            }
        });
    }

    private void saveStudyCreatedNotification(Study study, Account account) {
        Notification notification = new Notification();

        notification.setTitle(study.getTitle());
        notification.setLink("/study/" + study.getEncodePath());
        notification.setChecked(false);
        notification.setCreatedDateTime(LocalDateTime.now());
        notification.setMessage(study.getShortDescription());
        notification.setAccount(account);
        notification.setNotificationType(NotificationType.STUDY_CREATED);

        notificationRepository.save(notification);
    }

    private void sendStudyCreatedEmail(Study study, Account account) {
        Context context = new Context();

        context.setVariable("nickname", account.getNickname());
        context.setVariable("link", "/study/" + study.getEncodePath());
        context.setVariable("linkName", study.getTitle());
        context.setVariable("message", "새로운 스터디가 생겼습니다!");
        context.setVariable("host", appProperties.getHost());

        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .subject("스터디하자, '" + study.getTitle() + "' 스터디가 생겼습니다.")
                .to(account.getEmail())
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }
}
