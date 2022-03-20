package com.studyhaja.modules.account;

import com.studyhaja.infra.AbstractContainerBaseTest;
import com.studyhaja.infra.MockMvcTest;
import com.studyhaja.infra.mail.EmailMessage;
import com.studyhaja.infra.mail.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
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

@MockMvcTest
class AccountControllerTest extends AbstractContainerBaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @MockBean
    EmailService emailService;

    @Test
    @DisplayName("회원가입 화면이 나오는지 테스트")
    void signUpForm() throws Exception {
        this.mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated());
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
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
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
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername("robert"));

        assertTrue(accountRepository.existsByEmail("sungbin@email.com"));
        then(emailService).should().sendEmail(any(EmailMessage.class));

        Account account = accountRepository.findByEmail("sungbin@email.com");
        assertNotEquals(account.getPassword(), "12345678");

        assertNotNull(account.getEmailCheckToken());
    }

    @Test
    @DisplayName("인증 메일 확인 - 입력 값 오류")
    void checkEmailToken_with_wrong_input() throws Exception {
        this.mockMvc.perform(get("/check-email-token")
                        .param("token", "fdsfsdfsdfdf")
                        .param("email", "email@email.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("인증 메일 확인 - 입력 값 정상")
    void checkEmailToken() throws Exception {
        Account account = Account.builder()
                .nickname("sungbin")
                .email("test@email.com")
                .password("1234567")
                .build();

        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailCheckToken();

        this.mockMvc.perform(get("/check-email-token")
                        .param("token", newAccount.getEmailCheckToken())
                        .param("email", newAccount.getEmail()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(authenticated().withUsername("sungbin"));
    }
}
