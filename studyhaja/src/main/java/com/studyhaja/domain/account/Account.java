package com.studyhaja.domain.account;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * packageName : com.studyhaja.domain
 * fileName : Account
 * author : rovert
 * date : 2022/03/02
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/02       rovert         최초 생성
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email; // 이메일 :: 로그인 용도

    @Column(unique = true)
    private String nickname; // 닉네임 :: 이메일 대체 로그인 용도

    private String password; // 비밀번호

    private boolean emailVerified; // 이메일 인증이 된 계정인지 아닌지 플래그

    private String emailCheckToken; // 이메일을 검증할때 사용할 토큰 값

    private LocalDateTime joinedAt; // 이메일 인증 거친 사용자 가입 날짜

    private String bio; // 프로필 간단한 내 소개

    private String url; // 프로필 내 sns url이나 기타 사이트 url

    private String occupation; // 직업

    private String location; // 사는 곳

    @Lob
    @Basic
    private String profileImage; // 프로필 이미지 :: url 길어질것을 대비해 text타입으로 변경 및 기본 페치타입을 EAGER

    private boolean studyCreatedByEmail; // 스터디가 만들어진걸 이메일로 받을것인가?

    private boolean studyCreatedByWeb; // 스터디가 만들어진걸 웹으로 받을것인가?

    private boolean studyEnrollmentResultByEmail; // 스터디 등록 결과를 이메일로 받을것인가?

    private boolean studyEnrollmentResultByWeb; // 스터디 등록 결과를 웹으로 받을것인가?

    private boolean studyUpdatedByEmail; // 스터디 갱산 정보룰 이메일로 받을것인가?

    private boolean studyUpdatedByWeb; // 스터디 갱산 정보룰 웹으로 받을것인가?

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }
}
