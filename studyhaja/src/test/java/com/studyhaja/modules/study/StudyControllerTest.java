package com.studyhaja.modules.study;

import com.studyhaja.infra.MockMvcTest;
import com.studyhaja.modules.account.Account;
import com.studyhaja.modules.account.AccountFactory;
import com.studyhaja.modules.account.AccountRepository;
import com.studyhaja.modules.account.WithAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

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

@MockMvcTest
public class StudyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    StudyService studyService;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountFactory accountFactory;

    @Autowired
    StudyFactory studyFactory;


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
        Account account = accountFactory.createAccount("robert");
        Study study = studyFactory.createStudy("test-study", account);

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
        Account robert = accountFactory.createAccount("robert");
        Study study = studyFactory.createStudy("test-study", robert);

        Account sungbin = accountRepository.findByNickname("sungbin");
        studyService.addMember(study, sungbin);

        this.mockMvc.perform(get("/study/" + study.getPath() + "/leave"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/members"));

        assertFalse(study.getMembers().contains(sungbin));
    }
}