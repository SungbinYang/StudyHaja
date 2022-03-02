## 기초 점검
- 자바
    * final
    * static
    * equals / hashCode / toString
    * constructor
- 웹
    * HTML
    * CSS
    * JavaScript
    * JQuery
- 스프링
    * 스프링 부트
    * 스프링 MVC
    * 스프링 데이터 JPA
    * 스프링 시큐리티
- JPA
    * Transient / Persistent / Detached / Deleted
    * DBMS 설치 / 유저 또는 롤을 생성 / 데이터베이스 생성 / 권한 설정.
    * left join

## 인텔리J
- 인텔리J 단축키
    * 코드 생성
    * 소스 코드와 테스트 코드 이동 (또는 생성)
    * 퀵픽스
    * 클래스 찾기
    * 리팩토링
        * 리네임 리팩토링
        * 메소드 빼내기
        * 변수로 빼내기

## Git
- Git
    * checkout
        * 커밋 이동
        * 브랜치 이동
        * 브랜치 생성
    * stash
        * 변경 사항 다른 곳에 담아두기
        * 다시 적용하기

## 빌드툴
- 메이븐
    * 페이즈
    * 골
    * 의존성 추가
    * 인텔리J에서 리프레시

## 계정 관리 기능 구상
- 회원 가입
- 이메일 인증
- 로그인
- 로그아웃
- 프로필 추가 정보 입력
- 프로필 이미지 등록
- 알림 설정
- 패스워드 수정
- 패스워드를 잊어버렸습니다
- 관심 주제(태그) 등록
- 주요 활동 지역 등록

## 프로젝트 만들기
- IDE
    * 인텔리J 얼티메이트
- 빌드
    * 메이븐
- 라이브러리
    * 스프링 부트
    * 스프링 웹 MVC
    * 타임리프 (뷰 템플릿)
    * 스프링 시큐리티
    * 스프링 데이터 JPA
    * H2
    * PostgreSQL
    * 롬복
    * 스프링 mail
    * QueryDSL
    * 스프링 부트 devtools

## Account 도메인 클래스
- Account 도메인에 필요한 데이터
  * 로그인
  * 프로필
  * 알림 설정

## 회원 가입: 컨트롤러
- 목표
  * GET “/sign-up” 요청을 받아서 account/sign-up.html 페이지 보여준다.
  * 회원 가입 폼에서 입력 받을 수 있는 정보를 “닉네임", “이메일", “패스워드" 폼 객체로 제공한다.