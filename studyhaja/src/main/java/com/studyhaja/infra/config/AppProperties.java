package com.studyhaja.infra.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * packageName : com.studyhaja.config
 * fileName : AppProperties
 * author : rovert
 * date : 2022/03/12
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/12       rovert         최초 생성
 */

@Data
@Getter
@Setter
@Component
@ConfigurationProperties("app")
public class AppProperties {

    private String host;
}
