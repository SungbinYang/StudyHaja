package com.studyhaja.controller;

import com.studyhaja.ConsoleMailSender;
import com.studyhaja.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : com.studyhaja.controller
 * fileName : AccountControllerTest
 * author : rovert
 * date : 2022/03/02
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/02       rovert         최초 생성
 */

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    ConsoleMailSender consoleMailSender;

    @Test
    @DisplayName("회원가입 화면이 나오는지 테스트")
    void signUpForm() throws Exception {
        this.mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @Test
    @DisplayName("회원가입 처리 - 입력 값 오류")
    void signUpSubmit_with_wrong_input() throws Exception {
        this.mockMvc.perform(post("/sign-up")
                        .param("nickname", "robert")
                        .param("email", "email...")
                        .param("password", "12345")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @Test
    @DisplayName("회원가입 처리 - 입력 값 정상")
    void signUpSubmit_with_correct_input() throws Exception {
        this.mockMvc.perform(post("/sign-up")
                        .param("nickname", "robert")
                        .param("email", "sungbin@email.com")
                        .param("password", "12345678")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        assertTrue(accountRepository.existsByEmail("sungbin@email.com"));
        then(consoleMailSender).should().send(any(SimpleMailMessage.class));
    }
}