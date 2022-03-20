package com.studyhaja.infra.mail;

import lombok.Builder;
import lombok.Data;

/**
 * packageName : com.studyhaja.mail
 * fileName : EmailMessage
 * author : rovert
 * date : 2022/03/12
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/12       rovert         최초 생성
 */

@Data
@Builder
public class EmailMessage {

    private String to;

    private String subject;

    private String message;
}
