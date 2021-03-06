package com.studyhaja.modules.study.event;

import com.studyhaja.modules.study.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName : com.studyhaja.modules.study.event
 * fileName : StudyCreatedEvent
 * author : rovert
 * date : 2022/03/21
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/21       rovert         최초 생성
 */


@Getter
@RequiredArgsConstructor
public class StudyCreatedEvent {

    private final Study study;
}
