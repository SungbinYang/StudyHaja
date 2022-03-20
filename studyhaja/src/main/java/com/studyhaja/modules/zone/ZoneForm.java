package com.studyhaja.modules.zone;

import lombok.Data;

/**
 * packageName : com.studyhaja.domain.zone.form
 * fileName : ZoneForm
 * author : rovert
 * date : 2022/03/11
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/11       rovert         최초 생성
 */

@Data
public class ZoneForm {

    private String zoneName;

    public String getCityName() {
        return zoneName.substring(0, zoneName.indexOf("("));
    }

    public String getProvinceName() {
        return zoneName.substring(zoneName.indexOf("/") + 1);
    }

    public String getLocalNameOfCity() {
        return zoneName.substring(zoneName.indexOf("(") + 1, zoneName.indexOf(")"));
    }

    public Zone getZone() {
        return Zone.builder()
                .city(this.getCityName())
                .localNameOfCity(this.getLocalNameOfCity())
                .province(this.getProvinceName())
                .build();
    }
}
