package com.studyhaja.controller.study;

import com.studyhaja.common.WithAccount;
import com.studyhaja.domain.account.form.Account;
import com.studyhaja.domain.study.form.Study;
import com.studyhaja.repository.account.AccountRepository;
import com.studyhaja.repository.study.StudyRepository;
import com.studyhaja.service.study.StudyService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : com.studyhaja.controller.study
 * fileName : StudyControllerTest
 * author : rovert
 * date : 2022/03/13
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/13       rovert         최초 생성
 */

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class StudyControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected StudyService studyService;

    @Autowired
    protected StudyRepository studyRepository;

    @Autowired
    protected AccountRepository accountRepository;


    @Test
    @WithAccount("sungbin")
    @DisplayName("스터디 개설 폼 조회")
    void createStudyForm() throws Exception {
        this.mockMvc.perform(get("/new-study"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("studyForm"));
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("스터디 개설 - 완료")
    void createStudy_success() throws Exception {
        this.mockMvc.perform(post("/new-study")
                        .param("path", "test")
                        .param("title", "test title")
                        .param("shortDescription", "test shortDescription")
                        .param("fullDescription", "test fullDescription")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/test"));

        Study study = studyRepository.findByPath("test");
        assertNotNull(study);

        Account account = accountRepository.findByNickname("sungbin");
        assertTrue(study.getManagers().contains(account));
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("스터디 개설 - 실패")
    void createStudy_fail() throws Exception {
        this.mockMvc.perform(post("/new-study")
                        .param("path", "wrong path")
                        .param("title", "test title")
                        .param("shortDescription", "test shortDescription")
                        .param("fullDescription", "test fullDescription")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("studyForm"))
                .andExpect(model().attributeExists("account"));

        Study study = studyRepository.findByPath("test");
        assertNull(study);
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("스터디 조회")
    void viewStudy() throws Exception {
        Study study = new Study();
        study.setPath("test");
        study.setTitle("test title");
        study.setShortDescription("test shortDescription");
        study.setFullDescription("<p>test fullDescription</p>");

        Account sungbin = accountRepository.findByNickname("sungbin");
        studyService.createNewStudy(study, sungbin);

        this.mockMvc.perform(get("/study/test"))
                .andDo(print())
                .andExpect(view().name("study/view"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("study"));
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("스터디 가입")
    void joinStudy() throws Exception {
        Account account = createAccount("robert");
        Study study = createStudy("test-study", account);

        this.mockMvc.perform(get("/study/" + study.getPath() + "/join"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/members"));

        Account sungbin = accountRepository.findByNickname("sungbin");
        assertTrue(study.getMembers().contains(sungbin));
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("스터디 탈퇴")
    void leaveStudy() throws Exception {
        Account robert = createAccount("robert");
        Study study = createStudy("test-study", robert);

        Account sungbin = accountRepository.findByNickname("sungbin");
        studyService.addMember(study, sungbin);

        this.mockMvc.perform(get("/study/" + study.getPath() + "/leave"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/members"));

        assertFalse(study.getMembers().contains(sungbin));
    }



    protected Study createStudy(String path, Account account) {
        Study study = new Study();
        study.setPath(path);
        studyService.createNewStudy(study, account);

        return study;
    }

    protected Account createAccount(String nickname) {
        Account account = new Account();
        account.setNickname(nickname);
        account.setEmail(nickname + "@email.com");
        accountRepository.save(account);

        return account;
    }
}