# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080 으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리

* 구현 단계에서는 각 요구사항을 구현하는데 집중한다.
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다.

### 요구사항 1 - http://localhost:8080/index.html 로 접속시 응답

* HTTP request message 구조 <br>
  Reqeust Line(http method, request url, http version) <br>
  Header(key, value) <br>
  (Empty Line) <br>
  Body
* File Read - Files.readAllBytes(Path path)를 이용하여 읽고, 내부적으로 nio 라이브러리를 사용한다.(추후 nio 공부)

### 요구사항 2 - get 방식으로 회원가입

* `<from> 테그`의 method=get 으로 보낼시 request line 의 url값 뒤에 queryString 형태로 붙는다.

```code
/user/create?userId=jwkim.oa&password=1234&name=JiWon&email=jwkim.oa@gmail.com
````

### 요구사항 3 - post 방식으로 회원가입

* `<from> 테그`의 method=post 으로 보낼시 request body 에 queryString 형태로 담겨져 온다.

```code
userId=jwkim.oa&password=1234&name=JiWon&email=jwkim.oa@gmail.com
````

### 요구사항 4 - redirect 방식으로 이동

* Redirect Http response - `Location` header 에 redirect url 을 보낸다.

```code
HTTP/1.1 302 Found
Location: /index.html
```

### 요구사항 5 - cookie
* Set-Cookie : \<cookie-key\>=\<cookie-value\> 
* `Path=/` 를 통해 도메인 전역에 쿠키를 설정한다. ex) Set-Cookie : key=value; Path=/

### 요구사항 6 - stylesheet 적용
* CSS 파일을 브라우저가 인식 할 수 있도록 응답 헤더를 통해 Content-Type 을 알려줘야한다. <br>
  Content-Type: text/css
