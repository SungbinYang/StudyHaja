package com.studyhaja.domain.tag;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * packageName : com.studyhaja.domain.tag
 * fileName : Tag
 * author : rovert
 * date : 2022/03/10
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/10       rovert         최초 생성
 */

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id @GeneratedValue
    private Long id;

    private String title;
}
