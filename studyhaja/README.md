# 스터디 하자!

[구상 및 계획](./record.md)

## 프로젝트 설명
스터디 모임 관리에 대한 애플리케이션을 만들었다. 회원가입, 이메일 인증, 로그인등 기본적인 회원관리 기능부터
스터디 참가 모임 개설등 다양한 기능들을 담고 있다.

## 프로젝트 실행
- IDE에서 실행
    * IDE에서 프로젝트로 로딩한 다음에 메이븐으로 컴파일 빌드를 하고 App.java 클래스를 실행합니다.
- maven compile and build

    ```shell
    mvn compile
    ```

    * 메이븐으로 컴파일을 해야 프론트엔드 라이브러리를 받아오며 QueryDSL 관련 코드를 생성합니다.
- 콘솔 실행
    * jar 패키징 이후 java -jar로 실행

    ```shell
    mvn clean compile package
    ```
  
    ```shell
    java -jar target/*.jar
    ```
- 사용된 기술
    * 언어: JAVA 17
    * 프레임워크: Spring Boot, Spring Data JPA, Spring Security, Spring mail, Spring validation, QueryDsl
    * 라이브러리: 롬복
    * 테스트: JUnit5
    * DB: h2, postgresql
    * 뷰: HTML5, Javascript, CSS3, Bootstrap, JQuery, Thymeleaf
- 프로젝트 설정
    * flyway를 통한 db 형상관리를 하고있기 때문에 ddl의 변경 혹은 기본값 세팅등의 데이터베이스 설정이 변경될 경우 resources/db/migration 폴더에 sql 추가가 요구된다.
- DB 설정
  * PostgreSQL 설치 후, psql로 접속해서 아래 명령어 사용하여 DB와 USER 생성하고 권한 설정.

  ```sql
  CREATE DATABASE testdb;
  CREATE USER testuser WITH ENCRYPTED PASSWORD 'testpass';
  GRANT ALL PRIVILEGES ON DATABASE testdb TO testuser;
  ```