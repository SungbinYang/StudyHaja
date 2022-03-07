package com.studyhaja.domain.settings;

import com.studyhaja.domain.account.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class Profile {

    private String bio; // 프로필 간단한 내 소개

    private String url; // 프로필 내 sns url이나 기타 사이트 url

    private String occupation; // 직업

    private String location; // 사는 곳

    public Profile(Account account) {
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
    }
}
