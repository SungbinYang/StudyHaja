package com.studyhaja.modules.event.event;

import com.studyhaja.modules.event.Enrollment;

/**
 * packageName : com.studyhaja.modules.event.event
 * fileName : EnrollmentAcceptedEvent
 * author : rovert
 * date : 2022/03/22
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/22       rovert         최초 생성
 */

public class EnrollmentAcceptedEvent extends EnrollmentEvent {

    public EnrollmentAcceptedEvent(Enrollment enrollment) {
        super(enrollment, "모임 참가 신청을 확인했습니다. 모임에 참석하세요.");
    }
}
