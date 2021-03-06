package com.studyhaja.modules.event;

import com.studyhaja.modules.account.Account;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * packageName : com.studyhaja.domain.enrollment
 * fileName : Enrollment
 * author : rovert
 * date : 2022/03/14
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/14       rovert         최초 생성
 */

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(name = "Enrollment.withEventAndStudy", attributeNodes = {
        @NamedAttributeNode(value = "event", subgraph = "study")
}, subgraphs = @NamedSubgraph(name = "study", attributeNodes = @NamedAttributeNode("study")))
public class Enrollment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Account account;

    private LocalDateTime enrolledAt;

    private boolean accepted;

    private boolean attended;
}
