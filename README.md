# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* inputStream 으로 문자열 읽는 방법
```
    BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    String line = br.readLine();
``` 
* BufferedReader 를 readLine 을 하고, 다시 readLine 을 할 수 없다. 이미 포인터가 마지막에 있기 때문이다. 

* DataOutStream 에 내가 출력하길 원하는 내용 (텍스트, 파일) 등을 바이트로 변환하여 write 한다. 
  * responseBody 참고
  * response200Header 참고
  
* request Line
  * 클라이언트에서 서버로 보내는 요청의 첫 번째 라인
  * e.g. GET /index.html HTTP/1.1
* status Line
  * 서버에서 클라이언트로 보내는 응답의 첫 번째 라인
  * e.g. HTTP/1.1 200 OK
* 서버는 웹 페이지를 구성하는 모든 자원을 한번에 응답으로 보내지 않음
  * 여러 번의 요청과 응답을 주고 받게 됨.
  

### 요구사항 2 - get 방식으로 회원가입
* 

### 요구사항 3 - post 방식으로 회원가입
* post 로 요청할 때에도, body 도 queryString 방식으로 들어온다.
* e.g. username=tjrrb&password=password

### 요구사항 4 - redirect 방식으로 이동
* header 에 302 Found 로 저장하고, 
* header 에 Location : {tempUrl} 저장하면 
* 브라우저가 다시 tempUrl Get method 로 요청한다. 

### 요구사항 5 - cookie
* 서버는 헤더의 Set-Cookie 속성에 값을 넣어서 응답할 수 있다.
* 그러면 그 쿠키는 브라우저에 저장된다.
* 아무 속성을 주지 않았을 때, 이 쿠ㅎ키는 한 세션에서만 유지된다. 
  * 세션, 쿠키 차이점 
* final 키워드는 '한 번' 할당이 되고 변하지 않을 때 (혹은 변하지 않아야 할 때) 사용한다. 

### 요구사항 6 - 사용자 목록 출력하기
* Boolean.parseBoolean 은 JsonParse 처럼 에러를 던지지 않는다. 
  * 내부 구현을 확인해보면 "true".equalsIgnore(arg) 로 되어 있다. 
* http request cookies는 "logined=true; jsessionId=example" 이런 형식을 가진다.
* stringbuilder 로 html 형식의 string 을 생성 후 byte 변환하여 dataOutStream 에 write 하는 방식으로 유저 리스트를 반환 할 수 있다. 
* dataOutputStream
  * 추상 클래스인 outputStream 을 추가 구현한 클래스
  * 바이트 타입의 데이터를 쓸 수 있음. (기본적으로 outputStream 클래스의 Write 함수를 구현함.)
  * 바이트 타입 이외의 자바 타입도 쓸 수 있다. (e.g. writeBoolean, writeUTF, ...)

### 요구사항 7 - stylesheet 적용


### 요구사항 8 - refactoring code
* private 메소드의 복잡도가 높아 별도의 테스트가 필요한 데 테스트하기 힘들다면 어딘가 리팩토링할 부분이 있겠다. 
  * 여기서는 RequestLine class 를 별도로 분리해서 해결했다.  

* 경험이 많지 않은 상태에서는 일단 새로운 객체를 추가했으면 객체를 최대한 활용하기 위해 노력한다. 
  * 객체에서 값을 꺼낸 후 로직을 구현하려고 하지 말고, 가지고 있는 객체에 메시지를 보내 일을 시키도록 연습한다.
  * 객체를 최대한 활용했는 데 복잡도가 증가하고 책임이 점점 더 많아진다는 느낌이 드는 순간 새로운 객체를 추가한다.
* https://github.com/slipp/web-application-server 참고
* abstract class 에 꼭 abstract method 가 없어도 된다.
  * protected method 를 사용하여 조금 더 자유롭게 오버라이딩하도록 할 수 있다. 
    * 선택적으로 오버라이딩할 수 있다. 
  * abstract class 자체로 initialize 될 수 없다는 의의로 사용할 수 있다. 
* 