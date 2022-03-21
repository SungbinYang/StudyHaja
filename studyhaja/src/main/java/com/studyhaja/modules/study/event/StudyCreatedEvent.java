package com.studyhaja.modules.study.event;

import com.studyhaja.modules.study.Study;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
@AllArgsConstructor
public class StudyCreatedEvent {

    private Study study;
}
