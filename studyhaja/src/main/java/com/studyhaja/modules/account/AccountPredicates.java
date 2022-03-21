package com.studyhaja.modules.account;

import com.querydsl.core.types.Predicate;
import com.studyhaja.modules.tag.Tag;
import com.studyhaja.modules.zone.Zone;

import java.util.Set;

/**
 * packageName : com.studyhaja.modules.account
 * fileName : AccountPredicates
 * author : rovert
 * date : 2022/03/21
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/21       rovert         최초 생성
 */

public class AccountPredicates {

    public static Predicate findByTagsAndZones(Set<Tag> tags, Set<Zone> zones) {
        QAccount account = QAccount.account;

        return account.zones.any().in(zones).and(account.tags.any().in(tags));
    }
}
