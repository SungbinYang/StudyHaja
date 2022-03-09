package com.studyhaja.domain.settings.form;

import lombok.Data;

/**
 * packageName : com.studyhaja.domain.settings
 * fileName : Notifications
 * author : rovert
 * date : 2022/03/09
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/09       rovert         최초 생성
 */

@Data
public class Notifications {

    private boolean studyCreatedByEmail; // 스터디가 만들어진걸 이메일로 받을것인가?

    private boolean studyCreatedByWeb; // 스터디가 만들어진걸 웹으로 받을것인가?

    private boolean studyEnrollmentResultByEmail; // 스터디 등록 결과를 이메일로 받을것인가?

    private boolean studyEnrollmentResultByWeb; // 스터디 등록 결과를 웹으로 받을것인가?

    private boolean studyUpdatedByEmail; // 스터디 갱산 정보룰 이메일로 받을것인가?

    private boolean studyUpdatedByWeb; // 스터디 갱산 정보룰 웹으로 받을것인가?
}
