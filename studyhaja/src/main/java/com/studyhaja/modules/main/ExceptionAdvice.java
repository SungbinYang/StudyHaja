package com.studyhaja.modules.main;

import com.studyhaja.modules.account.Account;
import com.studyhaja.modules.account.CurrentAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * packageName : com.studyhaja.modules.main
 * fileName : ExceptionAdvice
 * author : rovert
 * date : 2022/03/26
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/26       rovert         최초 생성
 */

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public String handleRuntimeException(@CurrentAccount Account account, HttpServletRequest request, RuntimeException e) {

        if (account != null) {
            log.info("'{}' requested '{}'", account.getNickname(), request.getRequestURI());
        } else {
            log.info("requested '{}'", request.getRequestURI());
        }

        log.error("bad request", e);

        return "error";
    }
}
