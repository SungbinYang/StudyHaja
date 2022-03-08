package com.studyhaja.controller.settings;

import com.studyhaja.common.WithAccount;
import com.studyhaja.domain.account.Account;
import com.studyhaja.repository.account.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : com.studyhaja.controller.settings
 * fileName : SettingsControllerTest
 * author : rovert
 * date : 2022/03/07
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/07       rovert         최초 생성
 */

@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void after() {
        accountRepository.deleteAll();
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    void updateProfile() throws Exception {
        String bio = "짧은 소개를 수정하는 경우";
        this.mockMvc.perform(post("/" + SettingsController.SETTINGS_PROFILE_VIEW_NAME)
                        .param("bio", bio)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(flash().attributeExists("message"));

        Account sungbin = accountRepository.findByNickname("sungbin");
        assertEquals(bio, sungbin.getBio());
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("프로필 수정하기 - 입력값 에러")
    void updateProfile_error() throws Exception {
        String bio = "길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우";
        this.mockMvc.perform(post("/" + SettingsController.SETTINGS_PROFILE_VIEW_NAME)
                        .param("bio", bio)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account sungbin = accountRepository.findByNickname("sungbin");
        assertEquals(null, sungbin.getBio());
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("프로필 수정 폼")
    void updateProfileForm() throws Exception {
        String bio = "짧은 소개를 수정하는 경우";
        this.mockMvc.perform(get("/" + SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("패스워드 수정 폼")
    void updatePasswordForm() throws Exception {
        this.mockMvc.perform(get("/" + SettingsController.SETTINGS_PASSWORD_VIEW_NAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("패스워드 수정 - 입력 값 정상")
    void updatePassword() throws Exception {
        this.mockMvc.perform(post("/" + SettingsController.SETTINGS_PASSWORD_VIEW_NAME)
                        .param("newPassword", "12345678")
                        .param("newPasswordConfirm", "12345678")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + SettingsController.SETTINGS_PASSWORD_VIEW_NAME))
                .andExpect(flash().attributeExists("message"));

        Account sungbin = accountRepository.findByNickname("sungbin");
        assertTrue(passwordEncoder.matches("12345678", sungbin.getPassword()));
    }

    @Test
    @WithAccount("sungbin")
    @DisplayName("패스워드 수정 - 입력 값 에러")
    void updatePassword_error() throws Exception {
        this.mockMvc.perform(post("/" + SettingsController.SETTINGS_PASSWORD_VIEW_NAME)
                        .param("newPassword", "12345678")
                        .param("newPasswordConfirm", "87654321")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PASSWORD_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("passwordForm"));
    }

}