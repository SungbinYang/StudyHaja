package com.studyhaja.infra.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * packageName : com.studyhaja.service.mail
 * fileName : ConsoleEmailService
 * author : rovert
 * date : 2022/03/12
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/12       rovert         최초 생성
 */

@Slf4j
@Profile({"local", "test"})
@Component
public class ConsoleEmailService implements EmailService {

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        log.info("sent email: {}", emailMessage.getMessage());
    }
}
