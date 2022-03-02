package com.studyhaja.controller;

import com.studyhaja.domain.account.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
public class AccountController {

    @GetMapping("/sign-up")
    public String signUp(Model model) {

        return "account/sign-up";
    }
}
