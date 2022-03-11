package com.studyhaja.service.mail;

import com.studyhaja.mail.EmailMessage;

/**
 * packageName : com.studyhaja.service.mail
 * fileName : EmailService
 * author : rovert
 * date : 2022/03/12
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/12       rovert         최초 생성
 */

public interface EmailService {

    void sendEmail(EmailMessage emailMessage);
}
