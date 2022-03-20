package com.studyhaja.modules.main;

import com.studyhaja.modules.account.form.SignUpForm;
import com.studyhaja.modules.account.AccountRepository;
import com.studyhaja.modules.account.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * packageName : com.studyhaja.controller
 * fileName : MainControllerTest
 * author : rovert
 * date : 2022/03/06
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/06       rovert         최초 생성
 */

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void setup() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("sungbin");
        signUpForm.setEmail("sungbin@email.com");
        signUpForm.setPassword("12345678");
        accountService.processNewAccount(signUpForm);
    }

    @AfterEach
    void after() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일로 로그인하는 테스트")
    void login_with_email() throws Exception {

        this.mockMvc.perform(post("/login")
                        .param("username", "sungbin@email.com")
                        .param("password", "12345678")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("sungbin"));
    }

    @Test
    @DisplayName("닉네암으로 로그인하는 테스트")
    void login_with_nickname() throws Exception {

        this.mockMvc.perform(post("/login")
                        .param("username", "sungbin")
                        .param("password", "12345678")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("sungbin"));
    }

    @Test
    @DisplayName("로그인 실패")
    void login_fail() throws Exception {
        this.mockMvc.perform(post("/login")
                        .param("username", "s1212")
                        .param("password", "00000000")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser
    @DisplayName("로그아웃 테스트")
    void logout() throws Exception {
        this.mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

}