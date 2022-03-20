package com.studyhaja.modules.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * packageName : com.studyhaja.modules.account
 * fileName : AccountFactory
 * author : rovert
 * date : 2022/03/20
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/20       rovert         최초 생성
 */

@Component
public class AccountFactory {

    @Autowired
    AccountRepository accountRepository;

    public Account createAccount(String nickname) {
        Account account = new Account();
        account.setNickname(nickname);
        account.setEmail(nickname + "@email.com");

        accountRepository.save(account);

        return account;
    }
}
