package com.studyhaja.domain.account;

import lombok.Data;

/**
 * packageName : com.studyhaja.domain.account
 * fileName : SignUpForm
 * author : rovert
 * date : 2022/03/02
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/02       rovert         최초 생성
 */

@Data
public class SignUpForm {

    private String nickname;

    private String email;

    private String password;
}
