package com.studyhaja.domain.settings;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * packageName : com.studyhaja.domain.settings
 * fileName : PasswordForm
 * author : rovert
 * date : 2022/03/08
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/08       rovert         최초 생성
 */

@Data
public class PasswordForm {

    @Length(min = 8, max = 50)
    private String newPassword;

    @Length(min = 8, max = 50)
    private String newPasswordConfirm;
}
