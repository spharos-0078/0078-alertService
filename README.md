# 🔔 Alert Service

> **알림 서비스** - 실시간 알림 및 SSE 스트리밍을 담당하는 서비스

## 📋 목차

- [프로젝트 개요](#-프로젝트-개요)
- [기술 스택](#-기술-스택)
- [주요 기능](#-주요-기능)
- [알림 타입](#-알림-타입)
- [환경 설정](#-환경-설정)
- [실행 방법](#-실행-방법)
- [API 문서](#-api-문서)
- [팀원 정보](#-팀원-정보)

## 🎯 프로젝트 개요

Alert Service는 **실시간 알림 전송**과 **SSE(Server-Sent Events) 스트리밍**을 담당하는 서비스입니다.

### 핵심 기능
- **실시간 알림**: SSE 기반 실시간 알림 전송
- **공용 알림**: 모든 사용자 대상 공지사항
- **개인 알림**: 개별 사용자 맞춤 알림
- **알림 타입**: 12가지 알림 타입 지원
- **알림 히스토리**: 알림 기록 관리

### 알림 채널
- **SSE**: 실시간 브라우저 알림
- **FCM**: 모바일 푸시 알림 (향후 지원)
- **Email**: 이메일 알림 (향후 지원)
- **SMS**: 문자 알림 (향후 지원)

## 🛠️ 기술 스택

### Backend
- **Java 17** - LTS 버전
- **Spring Boot 3.5.3** - 최신 프레임워크
- **Spring WebFlux** - 반응형 웹 스택
- **Spring Data MongoDB** - NoSQL 데이터베이스
- **Spring Data MongoDB Reactive** - 반응형 MongoDB

### Database
- **MongoDB 4.4** - 문서형 데이터베이스
- **MongoDB Reactive** - 반응형 MongoDB 드라이버

### Message Queue
- **Apache Kafka 3.0** - 이벤트 스트리밍
- **Spring Kafka** - Kafka 연동

### Service Discovery
- **Netflix Eureka** - 서비스 등록/발견

### Documentation
- **SpringDoc OpenAPI 3** - API 문서화
- **Swagger UI** - API 테스트

## 🚀 주요 기능

### 1. 실시간 알림
- **SSE 연결**: 사용자별 실시간 연결 관리
- **알림 전송**: 실시간 알림 메시지 전송
- **연결 관리**: 연결 상태 모니터링 및 재연결
- **메시지 큐**: 오프라인 시 메시지 큐잉

### 2. 알림 타입 관리
- **공모 알림**: 공모 시작/종료, 참여 확인
- **거래 알림**: 거래 체결, 주문 취소
- **경매 알림**: 경매 시작/종료, 입찰 결과
- **시세 알림**: 가격 변동, 목표가 달성
- **시스템 알림**: 점검, 업데이트 공지

### 3. 개인화 알림
- **알림 설정**: 사용자별 알림 수신 설정
- **알림 필터**: 관심 상품, 카테고리별 필터
- **알림 히스토리**: 개인 알림 기록 관리
- **읽음 상태**: 알림 읽음/미읽음 상태 관리

### 4. 관리 기능
- **알림 발송**: 관리자 알림 발송 도구
- **알림 통계**: 발송 및 읽음 통계
- **알림 템플릿**: 알림 메시지 템플릿 관리
- **장애 대응**: 알림 발송 실패 시 재시도

## 🔔 알림 타입

### 공모 관련
- **FUNDING_STARTED**: 찜한 공모 시작
- **FUNDING_COMPLETED**: 참여 공모 완료
- **FUNDING_CANCELLED**: 참여 공모 취소

### 거래 관련
- **TRADE_EXECUTED**: 거래 체결
- **ORDER_CANCELLED**: 주문 취소
- **PRICE_ALERT**: 목표가 달성

### 경매 관련
- **AUCTION_STARTED**: 보유 상품 경매 시작
- **BID_OUTBID**: 입찰가 초과
- **AUCTION_WON**: 경매 낙찰

### 시스템 관련
- **SYSTEM_MAINTENANCE**: 시스템 점검
- **PRICE_CHANGE**: 가격 변동 알림
- **GENERAL_NOTICE**: 일반 공지사항

## ⚙️ 환경 설정

### 필수 요구사항
- Java 17 이상
- MongoDB 4.4 이상
- Kafka 3.0 이상
- Eureka Server 실행 중

### 환경 변수
- **MONGODB_URI**: MongoDB 연결 정보
- **KAFKA_HOST**: Kafka 호스트 정보
- **EUREKA_HOST**: Eureka 서버 정보

## 🚀 실행 방법

### 1. 저장소 클론
프로젝트 저장소를 로컬에 클론합니다.

### 2. 환경 설정
필요한 환경 변수를 설정합니다.

### 3. 데이터베이스 준비
MongoDB 데이터베이스와 컬렉션을 준비합니다.

### 4. 애플리케이션 실행
Gradle을 통해 애플리케이션을 실행합니다.

### 5. 접속 확인
- API 엔드포인트: http://localhost:8092
- Swagger UI: http://localhost:8092/swagger-ui/index.html

## 📚 API 문서

### Swagger UI 접속
API 문서는 Swagger UI를 통해 확인할 수 있습니다.

### 주요 API 엔드포인트

#### SSE 스트리밍
- **개인 알림 SSE**: 개인 알림 실시간 수신
- **공용 알림 SSE**: 전체 공지사항 실시간 수신

#### 알림 관리
- **알림 목록 조회**: 사용자별 알림 목록 조회
- **알림 상세 조회**: 특정 알림 상세 정보 조회
- **알림 발송**: 새로운 알림 발송
- **알림 읽음 처리**: 알림 읽음 상태 변경
- **알림 삭제**: 알림 삭제

#### 알림 설정
- **알림 설정 조회**: 사용자별 알림 설정 조회
- **알림 설정 수정**: 알림 수신 설정 변경

#### 통계
- **알림 통계**: 전체 알림 발송 통계
- **미읽음 알림 수**: 사용자별 미읽음 알림 수

## 👥 팀원 정보

**Piece of Cake 팀**
- **팀장**: 이수진 (Backend & Leader)
- **팀원**: 정동섭 (Backend), 이영인 (Backend), 오은서 (Backend), 정진우 (Frontend)

**Alert Service 담당**
- **정동섭**: 실시간 알림 시스템, SSE 스트리밍, 알림 관리

---

## 📞 연락처

- **프로젝트 홈페이지**: https://mobile.pieceofcake.site/
- **개발 기간**: 2025년 4월 30일 ~ 7월 10일

---

*"Investment is Easy and Fun" - Piece of Cake 프로젝트* 
