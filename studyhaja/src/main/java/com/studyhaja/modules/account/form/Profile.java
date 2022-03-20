package com.studyhaja.modules.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * packageName : com.studyhaja.domain.settings
 * fileName : Profile
 * author : rovert
 * date : 2022/03/06
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/06       rovert         최초 생성
 */

@Data
public class Profile {

    @Length(max = 35)
    private String bio; // 프로필 간단한 내 소개

    @Length(max = 50)
    private String url; // 프로필 내 sns url이나 기타 사이트 url

    @Length(max = 50)
    private String occupation; // 직업

    @Length(max = 50)
    private String location; // 사는 곳

    private String profileImage;
}
