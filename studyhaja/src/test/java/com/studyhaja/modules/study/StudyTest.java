package com.studyhaja.modules.study;

import com.studyhaja.modules.account.Account;
import com.studyhaja.modules.account.UserAccount;
import com.studyhaja.modules.study.Study;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * packageName : com.studyhaja.domain.study.form
 * fileName : StudyTest
 * author : rovert
 * date : 2022/03/13
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/13       rovert         최초 생성
 */

class StudyTest {

    Study study;

    Account account;

    UserAccount userAccount;

    @BeforeEach
    void setup() {
        study = new Study();
        account = new Account();
        account.setNickname("sungbin");
        account.setPassword("12345678");
        userAccount = new UserAccount(account);
    }

    @Test
    @DisplayName("스터디를 공개했고 인원 모집 중이고, 이미 멤버나 스터디 관리자가 아니라면 스터디 가입 가능")
    void isJoinable() {
        study.setPublished(true);
        study.setRecruiting(true);

        assertTrue(study.isJoinable(userAccount));
    }

    @Test
    @DisplayName("스터디를 공개했고 인원 모집 중이더라도, 스터디 관리자는 스터디 가입이 불필요하다.")
    void isJoinable_false_for_manager() {
        study.setPublished(true);
        study.setRecruiting(true);
        study.addManager(account);

        assertFalse(study.isJoinable(userAccount));
    }

    @Test
    @DisplayName("스터디를 공개했고 인원 모집 중이더라도, 스터디 멤버는 스터디 재가입이 불필요하다.")
    void isJoinable_false_for_member() {
        study.setPublished(true);
        study.setRecruiting(true);
        study.addMember(account);

        assertFalse(study.isJoinable(userAccount));
    }

    @Test
    @DisplayName("스터디가 비공개거나 인원 모집 중이 아니면 스터디 가입이 불가능하다.")
    void isJoinable_false_for_non_recruting_study() {
        study.setPublished(true);
        study.setRecruiting(false);

        assertFalse(study.isJoinable(userAccount));

        study.setPublished(false);
        study.setRecruiting(true);

        assertFalse(study.isJoinable(userAccount));
    }

    @Test
    @DisplayName("스터디 관리자인지 확인")
    void isManager() {
        study.addManager(account);
        assertTrue(study.isManager(userAccount));
    }

    @Test
    @DisplayName("스터디 멤버인지 확인")
    void isMember() {
        study.addMember(account);
        assertTrue(study.isMember(userAccount));
    }

}