package com.studyhaja.controller;

import com.studyhaja.domain.account.Account;
import com.studyhaja.domain.account.SignUpForm;
import com.studyhaja.domain.account.SignUpFormValidator;
import com.studyhaja.repository.AccountRepository;
import com.studyhaja.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

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

    private final AccountService accountService;

    private final AccountRepository accountRepository;

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

        accountService.processNewAccount(signUpForm);

        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);

        String view = "account/checked-email";
        if (account == null) {
            model.addAttribute("error", "wrong.email");

            return view;
        }

        if (!account.getEmailCheckToken().equals(token)) {
            model.addAttribute("error", "wrong.token");

            return view;
        }

        account.setEmailVerified(true);
        account.setJoinedAt(LocalDateTime.now());

        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());

        return view;
    }
}
