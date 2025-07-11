# Alert Service

## 📋 프로젝트 개요
Alert Service는 MSA(Microservice Architecture) 환경에서 실시간 알림 기능을 담당하는 마이크로서비스입니다. Kafka를 통해 다양한 이벤트를 구독하고, MongoDB Change Stream과 SSE(Server-Sent Events)를 활용하여 사용자에게 실시간 알림을 제공합니다.

## 🛠️ 기술 스택
- **Java 17**
- **Spring Boot 3.5.3**
- **Spring WebFlux** - 비동기 reactive programming
- **Spring Data MongoDB** - NoSQL 데이터베이스
- **Apache Kafka** - 이벤트 스트리밍 플랫폼
- **SSE (Server-Sent Events)** - 실시간 클라이언트 통신
- **MongoDB Change Stream** - 실시간 데이터 변경 감지
- **Spring Cloud Eureka** - 서비스 디스커버리
- **Gradle** - 빌드 도구
- **Docker** - 컨테이너화

## ✨ 주요 기능
- 📨 **이벤트 기반 알림 전송**: Kafka를 통해 회원가입, 투표 종료 등의 이벤트를 수신하여 알림 생성
- 🔄 **실시간 알림 스트리밍**: MongoDB Change Stream을 통해 새로운 알림을 실시간으로 감지
- 📡 **SSE 기반 실시간 통신**: 클라이언트와 실시간 양방향 통신 지원
- 🎯 **사용자별 개인화 알림**: 사용자 ID 기반 개인화된 알림 제공

## 🏗️ 프로젝트 구조
```
alert-service/
├── src/main/java/com/pieceofcake/alert_service/
│   ├── AlertServiceApplication.java         # 메인 애플리케이션
│   ├── alert/                               # 알림 도메인
│   │   ├── application/                     # 애플리케이션 서비스
│   │   ├── dto/                            # 데이터 전송 객체
│   │   ├── entity/                         # 엔티티 클래스
│   │   ├── infrastructure/                 # 인프라스트럭처 계층
│   │   ├── presentation/                   # 프레젠테이션 계층
│   │   └── vo/                            # 값 객체
│   ├── common/                             # 공통 모듈
│   │   ├── config/                         # 설정 클래스
│   │   ├── entity/                         # 공통 엔티티
│   │   └── exception/                      # 예외 처리
│   └── kafka/                              # Kafka 관련
│       ├── config/                         # Kafka 설정
│       ├── controller/                     # Kafka 컨트롤러
│       └── event/                          # 이벤트 클래스
├── src/main/resources/
│   ├── application.yml                     # 기본 설정
│   ├── application-dev.yml                 # 개발 환경 설정
│   └── application-local.yml               # 로컬 환경 설정
├── build.gradle                            # 빌드 설정
├── docker-compose.yml                      # Docker 컴포즈
├── Dockerfile                              # Docker 이미지
└── README.md                               # 프로젝트 문서
```

## 🚀 시작하기

### 사전 요구사항
- Java 17 이상
- Docker & Docker Compose
- MongoDB
- Apache Kafka

### 로컬 환경 설정
1. **저장소 클론**
   ```bash
   git clone [repository-url]
   cd alert-service
   ```

2. **환경 변수 설정**
   프로젝트 루트에 `.env` 파일을 생성하고 다음 내용을 추가하세요:
   ```env
   EC2_MONGOID=your_mongodb_username
   EC2_MONGOPW=your_mongodb_password
   EC2_HOST=your_eureka_server_host
   EC2_HOST2=your_kafka_server_host
   ```

3. **Docker 컨테이너 실행**
   ```bash
   docker-compose up -d
   ```

4. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

### 빌드 및 실행
```bash
# 빌드
./gradlew build

# 실행
./gradlew bootRun

# Docker 이미지 빌드
docker build -t alert-service .
```

## 📚 API 문서

### 엔드포인트
| Method | URI | 설명 |
|--------|-----|------|
| GET | `/stream/{userId}` | 사용자별 실시간 알림 스트리밍 (SSE) |
| GET | `/alert/list/{userId}` | 사용자 알림 목록 조회 |

### API 문서 접근
애플리케이션 실행 후 Swagger UI에서 API 문서를 확인할 수 있습니다:
- **Swagger UI**: `http://localhost:8300/swagger-ui/index.html`
- **API Docs**: `http://localhost:8300/v3/api-docs`

## 📡 Kafka 이벤트

### 구독 이벤트 (Consumer)
- `get-signup-data`: 회원가입 완료 알림
- `end-vote-alarm`: 투표 종료 알림
- 기타 사용자 대상 이벤트

### 이벤트 처리 흐름
1. Kafka에서 이벤트 수신
2. 이벤트 데이터를 파싱하여 알림 객체 생성
3. MongoDB에 알림 저장
4. MongoDB Change Stream이 변경 감지
5. 해당 사용자의 SSE 연결을 통해 실시간 전송

## 🔧 설정

### 주요 설정 파일
- `application.yml`: 기본 설정
- `application-dev.yml`: 개발 환경 설정
- `application-local.yml`: 로컬 환경 설정

### 환경별 프로파일
```yaml
# 개발 환경
spring.profiles.active: dev

# 로컬 환경
spring.profiles.active: local
```

## 🐳 Docker 배포

### Docker 컨테이너 실행
```bash
# 전체 서비스 실행
docker-compose up -d

# 특정 서비스만 실행
docker-compose up -d alert-service

# 로그 확인
docker-compose logs -f alert-service
```

### 환경 변수
Docker 환경에서 다음 환경 변수를 설정해야 합니다:
- `EC2_MONGOID`: MongoDB 사용자 ID
- `EC2_MONGOPW`: MongoDB 비밀번호
- `EC2_HOST`: Eureka 서버 호스트
- `EC2_HOST2`: Kafka 서버 호스트

## 🔍 모니터링 및 로깅

### 서비스 상태 확인
```bash
# 애플리케이션 상태
curl http://localhost:8300/actuator/health

# Eureka 서비스 등록 확인
curl http://localhost:8761/eureka/apps
```

### 로그 확인
```bash
# Docker 컨테이너 로그
docker-compose logs -f alert-service

# 애플리케이션 로그
tail -f logs/alert-service.log
```

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 `LICENSE` 파일을 확인하세요.

## 📞 연락처

프로젝트에 대한 질문이나 제안사항이 있으시면 언제든지 연락주세요.

---

**Piece of Cake Project** - MSA 기반 실시간 알림 서비스
