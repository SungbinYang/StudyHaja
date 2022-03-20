package com.studyhaja.infra;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * packageName : com.studyhaja.infra
 * fileName : AbstractContainerBaseTest
 * author : rovert
 * date : 2022/03/20
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/20       rovert         최초 생성
 */

public abstract class AbstractContainerBaseTest {

    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer();
        POSTGRE_SQL_CONTAINER.start();
    }
}
