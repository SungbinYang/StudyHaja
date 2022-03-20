package com.studyhaja.modules.account;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * packageName : com.studyhaja.common
 * fileName : WithAccount
 * author : rovert
 * date : 2022/03/07
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/07       rovert         최초 생성
 */

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAccountSecurityContextFactory.class)
public @interface WithAccount {

    String value();
}
