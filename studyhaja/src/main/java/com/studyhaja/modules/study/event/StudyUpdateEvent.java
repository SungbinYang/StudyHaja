package com.studyhaja.modules.study.event;

import com.studyhaja.modules.study.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName : com.studyhaja.modules.study.event
 * fileName : StudyUpdateEvent
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
public class StudyUpdateEvent {

    private final Study study;

    private final String message;
}
