# hello

Spring Boot로 만든 인사 서비스입니다. 로그인하면 `Hello. {아이디}`를 보여 주고, 로그인 없이 쓸 수 있는 인사 API도 제공합니다.

## 요구 사항

- Java 21 이상
- Maven은 따로 설치하지 않아도 됩니다. 포함된 Maven Wrapper(`./mvnw`)를 사용합니다.

## 실행

```bash
./mvnw spring-boot:run
```

http://localhost:8080 에 접속해 아래 개발용 계정으로 로그인합니다.

| 아이디 | 비밀번호 |
|---|---|
| `jacob` | `demo1234` |

## 설정

계정은 환경변수로 바꿀 수 있습니다. 환경변수가 없으면 위 개발용 계정이 쓰입니다.

| 환경변수 | 기본값 | 설명 |
|---|---|---|
| `APP_USERNAME` | `jacob` | 로그인 아이디 |
| `APP_PASSWORD` | `demo1234` | 로그인 비밀번호. 평문 또는 `{bcrypt}$2a$10$...` 같은 해시값 |

```bash
APP_USERNAME=admin APP_PASSWORD=원하는비번 ./mvnw spring-boot:run
```

> 기본 비밀번호 `demo1234`는 로컬 개발용입니다. 다른 사람이 접속하는 곳에 배포할 때는 반드시 `APP_PASSWORD`로 바꿔 주세요.

## 화면과 API

| 경로 | 로그인 필요 | 설명 |
|---|---|---|
| `GET /login` | 아니오 | 로그인 화면 |
| `GET /` | 예 | 홈 화면. `Hello. {아이디}`와 로그아웃 버튼 |
| `POST /logout` | 예 | 로그아웃 후 `/login?logout`으로 이동 |
| `GET /api/hello?name={이름}` | 아니오 | 인사 JSON 반환 |
| `GET /actuator/health` | 아니오 | 상태 확인 |

### 인사 API

```bash
curl localhost:8080/api/hello
# {"message":"Hello. Jacob"}

curl "localhost:8080/api/hello?name=Tom"
# {"message":"Hello. Tom"}
```

`name`이 비어 있거나 50자를 넘으면 `400 Bad Request`와 함께 [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) 형식의 오류를 반환합니다.

## 테스트

```bash
./mvnw test
```

## 프로젝트 구조

```text
src/main/java/com/example/hello/
├── auth/LoginController.java        로그인 화면
├── config/SecurityConfig.java       접근 규칙, 로그인·로그아웃, CSP 헤더
├── greeting/                        인사 API (Controller, Service, Greeting)
└── home/HomeController.java         로그인 후 홈 화면
src/main/resources/
├── application.properties
├── static/                          CSS, 파비콘
└── templates/                       Thymeleaf 템플릿 (login, home)
```

## 기술 스택

- Spring Boot 4.1 (Web MVC, Security, Validation, Actuator)
- Thymeleaf
- JUnit 5, Spring Security Test
