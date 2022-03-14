package com.studyhaja.controller.study;

import com.studyhaja.common.WithAccount;
import com.studyhaja.domain.account.form.Account;
import com.studyhaja.domain.study.form.Study;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : com.studyhaja.controller.study
 * fileName : StudySettingsControllerTest
 * author : rovert
 * date : 2022/03/14
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/14       rovert         최초 생성
 */
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class StudySettingsControllerTest extends StudyControllerTest {

    @Test
    @WithAccount("sungbin")
    @DisplayName("스터디 소개 수정 폼 조회 - 실패 (권한 없는 유저)")
    void updateDescriptionForm_fail() throws Exception {
        Account robert = createAccount("robert");
        Study study = createStudy("test-study", robert);

        this.mockMvc.perform(get("/study/" + study.getPath() + "/settings/description"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("스터디 소개 수정 폼 조회 - 성공")
    void updateDescriptionForm() throws Exception {
        Account sungbin = accountRepository.findByNickname("sungbin");
        Study study = createStudy("test-study", sungbin);

        this.mockMvc.perform(get("/study/" + study.getPath() + "/settings/description"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("study/settings/description"))
                .andExpect(model().attributeExists("studyDescriptionForm"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("study"));
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("스터디 소개 수정 조회 - 성공")
    void updateDescription() throws Exception {
        Account sungbin = accountRepository.findByNickname("sungbin");
        Study study = createStudy("test-study", sungbin);

        String settingsDescriptionUrl = "/study/" + study.getPath() + "/settings/description";

        this.mockMvc.perform(post(settingsDescriptionUrl)
                        .param("shortDescription", "짧은 글")
                        .param("fullDescription", "상세 내용")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(settingsDescriptionUrl))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("스터디 소개 수정 조회 - 실패")
    void updateDescription_fail() throws Exception {
        Account sungbin = accountRepository.findByNickname("sungbin");
        Study study = createStudy("test-study", sungbin);

        String settingsDescriptionUrl = "/study/" + study.getPath() + "/settings/description";

        this.mockMvc.perform(post(settingsDescriptionUrl)
                        .param("shortDescription", "")
                        .param("fullDescription", "상세 내용")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("studyDescriptionForm"))
                .andExpect(model().attributeExists("study"))
                .andExpect(model().attributeExists("account"));
    }
}