package com.studyhaja.modules.event.event;

import com.studyhaja.modules.event.Enrollment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName : com.studyhaja.modules.event.event
 * fileName : EnrollmentEvent
 * author : rovert
 * date : 2022/03/22
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/22       rovert         최초 생성
 */

@Getter
@RequiredArgsConstructor
public abstract class EnrollmentEvent {

    protected final Enrollment enrollment;

    protected final String message;
}
