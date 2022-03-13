package com.studyhaja.domain.study.form;

import com.studyhaja.domain.account.form.Account;
import com.studyhaja.domain.account.form.UserAccount;
import com.studyhaja.domain.tag.form.Tag;
import com.studyhaja.domain.zone.form.Zone;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * packageName : com.studyhaja.domain.study.form
 * fileName : Study
 * author : rovert
 * date : 2022/03/12
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/12       rovert         최초 생성
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(name = "Study.withAll", attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("zones"),
        @NamedAttributeNode("managers"),
        @NamedAttributeNode("members")
})
@NamedEntityGraph(name = "Study.withTagsAndManagers", attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("managers")
})
@NamedEntityGraph(name = "Study.withZonesAndManagets", attributeNodes = {
        @NamedAttributeNode("zones"),
        @NamedAttributeNode("managers")
})
public class Study {

    @Id @GeneratedValue
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob
    @Basic
    private String fullDescription;

    @Lob
    @Basic
    private String image;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime; // 시간제한을 둘 필요성이 있음

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;

    public void addManager(Account account) {
        this.managers.add(account);
    }

    public void addMember(Account account) {
        this.members.add(account);
    }

    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount();

        return this.isPublished() && this.isRecruiting() && !this.members.contains(account) && !this.managers.contains(account);
    }

    public boolean isMember(UserAccount userAccount) {
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount) {
        return this.managers.contains(userAccount.getAccount());
    }

    public String getImage() {
        return image != null ? image : "/images/defaultBanner.jpeg";
    }
}
