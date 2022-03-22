package com.studyhaja.modules.event.event;

import com.studyhaja.modules.event.Enrollment;

/**
 * packageName : com.studyhaja.modules.event.event
 * fileName : EnrollmentRejectedEvent
 * author : rovert
 * date : 2022/03/22
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/22       rovert         최초 생성
 */

public class EnrollmentRejectedEvent extends EnrollmentEvent {

    public EnrollmentRejectedEvent(Enrollment enrollment) {
        super(enrollment, "모임 참가 신청을 거절했습니다.");
    }
}
