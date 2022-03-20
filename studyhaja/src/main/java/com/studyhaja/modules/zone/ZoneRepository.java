package com.studyhaja.modules.zone;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName : com.studyhaja.repository.zone
 * fileName : ZoneRepository
 * author : rovert
 * date : 2022/03/11
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/11       rovert         최초 생성
 */

public interface ZoneRepository extends JpaRepository<Zone, Long> {

    Zone findByCityAndProvince(String cityName, String provinceName);
}
