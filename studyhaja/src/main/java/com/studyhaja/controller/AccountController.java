package com.studyhaja.controller;

import com.studyhaja.ConsoleMailSender;
import com.studyhaja.domain.account.Account;
import com.studyhaja.domain.account.SignUpForm;
import com.studyhaja.domain.account.SignUpFormValidator;
import com.studyhaja.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * packageName : com.studyhaja.controller
 * fileName : AccountController
 * author : rovert
 * date : 2022/03/02
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/02       rovert         최초 생성
 */

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;

    private final AccountRepository accountRepository;

    private final ConsoleMailSender consoleMailSender;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUp(Model model, Account account) {
        model.addAttribute(new SignUpForm());

        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) { // 에러가 있는 경우
            return "account/sign-up";
        }

        //TODO:  회원가입 처리
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(signUpForm.getPassword()) // TODO: password encoding
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();

        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailCheckToken();

        // TODO: email 보내기 기능
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("스터디하자, 회원가입 인증");
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() +  "&email=" + newAccount.getEmail());
        consoleMailSender.send(mailMessage);

        return "redirect:/";
    }
}
