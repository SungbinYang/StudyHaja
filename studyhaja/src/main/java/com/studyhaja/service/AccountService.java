package com.studyhaja.service;

import com.studyhaja.domain.account.Account;
import com.studyhaja.domain.account.SignUpForm;
import com.studyhaja.domain.account.UserAccount;
import com.studyhaja.mail.ConsoleMailSender;
import com.studyhaja.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName : com.studyhaja.service
 * fileName : AccountService
 * author : rovert
 * date : 2022/03/03
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/03       rovert         최초 생성
 */

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final ConsoleMailSender consoleMailSender;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();

        // TODO: email 보내기 기능
        sendSignUpConfirmEmail(newAccount);

        return newAccount;
    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        //TODO:  회원가입 처리
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword())) // TODO: password encoding
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();

        return accountRepository.save(account);
    }

    private void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("스터디하자, 회원가입 인증");
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() +  "&email=" + newAccount.getEmail());
        consoleMailSender.send(mailMessage);
    }

    public void login(Account account) {
        // 정석적인 방법은 아님 :: 원래는 AuthenticationManager에서 사용해야함
        // 정석적인 방법을 사용하지 못하는 이유는 패스워드를 인코딩한 패스워드밖에 접근이 불가능하기 때문이다.
        // 정석적인 방법은 그 플레인 텍스트로 받은 비밀번호를 써야한다.
        // 로그인 로직
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(new UserAccount(account),
                account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
