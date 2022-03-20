package com.studyhaja.modules.zone;

import lombok.*;

import javax.persistence.*;

/**
 * packageName : com.studyhaja.domain.zone.form
 * fileName : Zone
 * author : rovert
 * date : 2022/03/11
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/11       rovert         최초 생성
 */

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"city", "province"}))
public class Zone {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String localNameOfCity;

    @Column
    private String province;

    @Override
    public String toString() {
        return String.format("%s(%s)/%s", city, localNameOfCity, province);
    }
}
