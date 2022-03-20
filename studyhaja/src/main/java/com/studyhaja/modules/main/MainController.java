package com.studyhaja.modules.main;

import com.studyhaja.modules.account.CurrentAccount;
import com.studyhaja.modules.account.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * packageName : com.studyhaja.main
 * fileName : MainController
 * author : rovert
 * date : 2022/03/06
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/06       rovert         최초 생성
 */

@Controller
public class MainController {

    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
