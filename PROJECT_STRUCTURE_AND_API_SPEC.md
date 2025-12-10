# MinjuLog Backend - 프로젝트 구조 및 API 명세서

## 📋 프로젝트 개요

**프로젝트명**: MinjuLog Backend (민주 로그 백엔드)
**기술 스택**:
- Java 17
- Spring Boot 3.3.5
- Spring Data JPA
- QueryDSL
- MySQL 8.0
- Redis
- Swagger 3 (OpenAPI)
- Bucket4j (Rate Limiting)

**브랜치**: `feature/proto-v1`

---

## 🏗️ 프로젝트 아키텍처

### 레이어 구조

```
src/main/java/com/server/
├── presentation/          # Controller 레이어 (HTTP 요청 처리)
│   ├── user/
│   ├── proposal/
│   │   ├── proposal/
│   │   ├── vote/
│   │   └── signature/
│   └── converter/         # Request → Command 변환
├── application/           # Service 레이어 (비즈니스 로직)
│   ├── service/
│   │   ├── user/
│   │   └── proposal/
│   └── usecase/          # UseCase 패턴 구현
│       ├── user/
│       └── proposal/
├── domain/               # Domain 레이어 (엔티티, 저장소)
│   ├── entity/           # JPA 엔티티
│   │   ├── user/
│   │   ├── proposal/
│   │   ├── tag/
│   │   └── policy/
│   ├── repository/       # Data Access Layer
│   │   ├── user/
│   │   └── proposal/
│   └── test/            # 테스트용 샘플 코드
└── global/              # 글로벌 설정 및 유틸
    ├── config/          # 설정 클래스
    ├── common/          # 공통 클래스
    │   ├── base/        # BaseEntity, BaseResponse
    │   ├── exception/   # 예외 처리
    │   ├── dto/
    │   └── util/
    └── ratelimit/       # Rate Limit 설정
```

### 데이터 흐름

```
HTTP Request
    ↓
Controller (presentation)
    ↓
ApplicationService + Converter (application)
    ↓
UseCase Service (application/usecase)
    ↓
Repository (domain/repository)
    ↓
Database (MySQL)
```

---

## 📊 데이터 모델

### Entity 관계도

```
User (사용자)
├── 1: N → Proposal (제안)
│        ├── N: 1 → Topic (토픽)
│        ├── 1: N → ProposalVote (투표)
│        └── 1: N → ProposalSignature (서명)
├── 1: N → ProposalVote
├── 1: N → ProposalSignature
│
Topic (토픽)
└── 1: N → Proposal (제안)

PolicyCase (정책 사례)
├── 1: N → PolicyCaseTag (태그)
└── 1: N → PolicyCaseLike (좋아요)

Tag (태그)
└── 통과 Proposal 의 hashtags (ElementCollection)
```

### Entity 상세 정보

#### 1. **User** (사용자)
```java
@Entity
@Table(name = "user")
- id: Long (PK)
- nickname: String (필수)
- createdAt: LocalDateTime (BaseEntity 상속)
- updatedAt: LocalDateTime (BaseEntity 상속)
```
- **설명**: 앱 사용자를 나타냄. 현재는 익명 사용자만 지원
- **관계**: Proposal, ProposalVote, ProposalSignature 의 작성자

---

#### 2. **Proposal** (제안)
```java
@Entity
@Table(name = "proposal")
- id: Long (PK)
- user_id: Long (FK - User)
- topic_id: Long (FK - Topic, 선택사항)
- status: ProposalStatus (Enum: COLLECTING, DELIVERED, REPORTING, COMPLETED)
- title: String (필수, 제안 제목)
- body: String (필수, TEXT, 제안 내용)
- viewCount: Long (조회수)
- hashtags: List<String> (ElementCollection, 해시태그 목록)
- dueDate: LocalDate (투표 종료일)
- createdAt: LocalDateTime (BaseEntity 상속)
- updatedAt: LocalDateTime (BaseEntity 상속)
```
- **설명**: 사용자가 작성한 정책 제안
- **메서드**:
  - `increaseViewCount()`: 조회수 증가
  - `changeStatus(ProposalStatus)`: 상태 변경
- **관계**: 
  - N: 1 → User (작성자)
  - N: 1 → Topic (선택사항, 토픽 분류)
  - 1: N → ProposalVote (투표)
  - 1: N → ProposalSignature (서명)

---

#### 3. **ProposalVote** (투표)
```java
@Entity
@Table(name = "proposal_like")
- id: Long (PK)
- proposal_id: Long (FK - Proposal)
- user_id: Long (FK - User)
- voteType: ProposalVoteType (Enum: AGREE, DISAGREE)
- createdAt: LocalDateTime (BaseEntity 상속)
- updatedAt: LocalDateTime (BaseEntity 상속)
```
- **설명**: 사용자가 제안에 투표한 정보
- **관계**:
  - N: 1 → Proposal
  - N: 1 → User

---

#### 4. **ProposalSignature** (서명)
```java
@Entity
@Table(name = "proposal_signature")
- id: Long (PK)
- proposal_id: Long (FK - Proposal)
- user_id: Long (FK - User)
- nickname: String (필수, 서명 시점의 닉네임)
- content: String (필수, 서명 내용/의견)
- signatureType: ProposalSignatureType (Enum: AGREE, DISAGREE)
- createdAt: LocalDateTime (BaseEntity 상속)
- updatedAt: LocalDateTime (BaseEntity 상속)
```
- **설명**: 사용자가 제안에 서명한 내용 (의견 기재)
- **특징**: 서명할 때마다 요청 바디에서 제공된 닉네임으로 업데이트됨
- **관계**:
  - N: 1 → Proposal
  - N: 1 → User

---

#### 5. **Topic** (토픽)
```java
@Entity
@Table(name = "topic")
- id: Long (PK)
- name: String (필수, 유니크, 토픽 이름)
- description: String (선택사항, 토픽 설명)
- createdAt: LocalDateTime (BaseEntity 상속)
- updatedAt: LocalDateTime (BaseEntity 상속)
```
- **설명**: 제안을 분류하기 위한 토픽/카테고리
- **관계**: 1: N → Proposal (제안)

---

#### 6. **PolicyCase** (정책 사례)
```java
@Entity
@Table(name = "policy_case")
- id: Long (PK)
- title: String (필수, 정책 사례 제목)
- body: String (필수, TEXT, 정책 사례 내용)
- createdAt: LocalDateTime
- tags: List<PolicyCaseTag> (1: N 관계)
```
- **설명**: 정책 반영 사례
- **관계**: 
  - 1: N → PolicyCaseTag
  - 1: N → PolicyCaseLike

---

### Enum 타입

#### 1. **ProposalStatus**
```
COLLECTING(의견 취합중)      → 초기 상태, 투표/서명 진행 중
DELIVERED(의견 전달 완료)    → 의견이 관련 기관에 전달됨
REPORTING(보도중)           → 언론 보도 중
COMPLETED(반영 완료)        → 최종 상태, 정책 반영 완료
```

#### 2. **ProposalVoteType**
```
AGREE    → 찬성
DISAGREE → 반대
```

#### 3. **ProposalSignatureType**
```
AGREE    → 찬성 (동의하는 의견)
DISAGREE → 반대 (반대하는 의견)
```

---

## 🔌 API 명세서

### 기본 설정
- **Base URL**: `http://localhost:8080`
- **API Prefix**: `/api`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **Response Format**: 모든 응답은 `BaseResponse` 래핑

### BaseResponse 구조
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": {
    // API별 응답 데이터
  }
}
```

---

### 0️⃣ **Topic API** (`/api/topics`)

#### 0.1 토픽 목록 조회
```http
GET /api/topics
```

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": [
    {
      "id": 1,
      "name": "교육",
      "description": "교육 관련 정책",
      "createdAt": "2024-11-25T10:30:00"
    },
    {
      "id": 2,
      "name": "인프라",
      "description": "인프라 관련 정책",
      "createdAt": "2024-11-25T10:30:00"
    }
  ]
}
```

**설명**: 모든 토픽 목록 조회

---

#### 0.2 토픽 상세 조회
```http
GET /api/topics/{id}
```

**파라미터**:
- `id` (path): 토픽 ID

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": {
    "id": 1,
    "name": "교육",
    "description": "교육 관련 정책",
    "createdAt": "2024-11-25T10:30:00"
  }
}
```

**설명**: 특정 토픽 상세 정보 조회

---

#### 0.3 토픽 삭제
```http
DELETE /api/topics/{id}
```

**파라미터**:
- `id` (path): 토픽 ID

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": true
}
```

**설명**: 토픽 삭제 (관리자 기능)

---

### 1️⃣ **User API** (`/api/users`)
```http
POST /api/users
Content-Type: application/json
```

**요청**:
```json
{}
```

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": {
    "userId": 1,
    "nickname": "사용자123",
    "createdAt": "2024-11-25T10:30:00"
  }
}
```

**설명**: 랜덤한 닉네임으로 익명 사용자 생성

---

### 2️⃣ **Proposal API** (`/api/proposals`)

#### 2.1 제안 생성
```http
POST /api/proposals
Content-Type: application/json
```

**요청**:
```json
{
  "userId": 1,
  "title": "공공 와이파이 확대",
  "body": "모든 공중시설에 무료 와이파이를 제공해야 합니다...",
  "topicId": 1,
  "hashtags": ["통신", "인프라"],
  "dueDate": "2024-12-31"
}
```

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": {
    "proposalId": 1,
    "title": "공공 와이파이 확대",
    "body": "모든 공중시설에 무료 와이파이를 제공해야 합니다...",
    "status": "COLLECTING",
    "hashtags": ["통신", "인프라"],
    "dueDate": "2024-12-31",
    "createdAt": "2024-11-25T10:30:00"
  }
}
```

**설명**: 새로운 정책 제안 생성
- **topicId**: 선택사항으로 제안을 토픽으로 분류할 수 있음

---

#### 2.2 제안 상세 조회
```http
GET /api/proposals/{id}?userId={userId}
```

**파라미터**:
- `id` (path): 제안 ID
- `userId` (query): 조회하는 사용자 ID (투표/서명 여부 확인용)

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": {
    "proposalId": 1,
    "title": "공공 와이파이 확대",
    "body": "...",
    "status": "COLLECTING",
    "hashtags": ["통신", "인프라"],
    "topicId": 1,
    "topicName": "인프라",
    "dueDate": "2024-12-31",
    "viewCount": 150,
    "creator": {
      "userId": 1,
      "nickname": "사용자123"
    },
    "voteCount": {
      "agree": 45,
      "disagree": 5
    },
    "signatureCount": 32,
    "userVoted": true,
    "userVoteType": "AGREE",
    "userSigned": true,
    "createdAt": "2024-11-25T10:30:00",
    "updatedAt": "2024-11-25T10:30:00"
  }
}
```

**설명**: 제안 상세 정보 조회 (조회수 증가)

---

#### 2.3 제안 목록 조회
```http
GET /api/proposals?keyword={keyword}&status={status}&hashtag={hashtag}&sort={sort}&page={page}&size={size}
```

**파라미터**:
- `keyword` (query, 선택): 검색 키워드 (제목/본문/해시태그 모두 포함)
- `status` (query, 선택): 제안 상태 필터
  - `COLLECTING`: 의견 취합중
  - `DELIVERED`: 의견 전달 완료
  - `REPORTING`: 보도중
  - `COMPLETED`: 반영 완료
- `hashtag` (query, 선택): 해시태그 필터
- `sort` (query, 기본값: latest): 정렬 방식
  - `latest`: 최신순 (기본값)
  - `popular`: 인기순 ((찬성 서명 + 찬성 투표) - (반대 서명 + 반대 투표))
  - `views`: 조회수순
  - `agree-signature`: 찬성 서명순
  - `disagree-signature`: 반대 서명순
  - `agree-vote`: 찬성 투표순
  - `disagree-vote`: 반대 투표순
- `page` (query, 기본값: 0): 페이지 번호 (0부터 시작)
- `size` (query, 기본값: 10): 페이지 크기

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": {
    "content": [
      {
        "proposalId": 1,
        "title": "공공 와이파이 확대",
        "body": "학생들을 위해 급식을 개선해주세요...",
        "status": "COLLECTING",
        "hashtags": ["통신", "인프라"],
        "topicId": 1,
        "topicName": "인프라",
        "viewCount": 150,
        "voteCount": {
          "agree": 45,
          "disagree": 5
        },
        "signatureCount": 32,
        "creator": {
          "userId": 1,
          "nickname": "사용자123"
        },
        "createdAt": "2024-11-25T10:30:00"
      },
      // ... 더 많은 항목
    ],
    "totalElements": 150,
    "totalPages": 15,
    "number": 0,
    "size": 10,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

**설명**: 필터/정렬 조건에 따른 제안 목록 조회
- **검색 범위**: keyword가 제목, 본문, 해시태그 중 어느 하나에 포함되면 검색됨
- **totalElements**: 필터링된 결과의 전체 개수 (status와 hashtag 필터 적용 후)

---

#### 2.4 해시태그 기반 제안 조회
```http
GET /api/proposals/by-hashtag?name={tagName}&page={page}&size={size}
```

**파라미터**:
- `name` (query, 필수): 해시태그 이름
- `page` (query, 기본값: 0): 페이지 번호
- `size` (query, 기본값: 10): 페이지 크기

**응답**: 제안 목록 조회와 동일한 형식

**설명**: 특정 해시태그로 필터링된 제안 목록 조회

---

#### 2.5 제안 상태 변경 (관리자)
```http
PATCH /api/proposals/{id}/status
Content-Type: application/json
```

**파라미터**:
- `id` (path): 제안 ID

**요청**:
```json
{
  "status": "DELIVERED"
}
```

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": true
}
```

**설명**: 제안의 상태를 변경 (COLLECTING → DELIVERED → REPORTING → COMPLETED)

---

### 3️⃣ **Vote API** (`/api/proposals/{id}/votes`)

#### 3.1 투표하기
```http
POST /api/proposals/{proposalId}/votes
Content-Type: application/json
```

**파라미터**:
- `proposalId` (path): 제안 ID

**요청**:
```json
{
  "userId": 1,
  "voteType": "AGREE"
}
```

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": {
    "voteId": 100,
    "voteType": "AGREE"
  }
}
```

**설명**: 제안에 투표 (찬성/반대)
- **규칙**: 사용자당 제안 하나에 한 번만 투표 가능
- 중복 투표 시 기존 투표 업데이트

---

#### 3.2 투표 취소
```http
DELETE /api/proposals/{proposalId}/votes?userId={userId}
```

**파라미터**:
- `proposalId` (path): 제안 ID
- `userId` (query): 사용자 ID

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": true
}
```

**설명**: 제안에 대한 투표 취소

---

### 4️⃣ **Signature API** (`/api/proposals/{id}/signatures`)

#### 4.1 서명 등록
```http
POST /api/proposals/{proposalId}/signatures
Content-Type: application/json
```

**파라미터**:
- `proposalId` (path): 제안 ID

**요청**:
```json
{
  "userId": 1,
  "nickname": "익명사용자123",
  "content": "이 정책이 정말 필요합니다. 적극 동의합니다.",
  "signatureType": "AGREE"
}
```

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": {
    "signatureId": 50,
    "signatureType": "AGREE",
    "content": "이 정책이 정말 필요합니다. 적극 동의합니다.",
    "createdAt": "2024-11-25T10:30:00"
  }
}
```

**설명**: 제안에 대한 의견(서명) 등록
- **규칙**: 사용자당 제안 하나에 한 번만 서명 가능
- **닉네임 업데이트**: 중복 서명 시 기존 서명의 닉네임과 내용을 요청 바디의 닉네임으로 업데이트

---

#### 4.2 서명 취소
```http
DELETE /api/proposals/{proposalId}/signatures?userId={userId}
```

**파라미터**:
- `proposalId` (path): 제안 ID
- `userId` (query): 사용자 ID

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": true
}
```

**설명**: 제안에 대한 서명 취소

---

#### 4.3 서명 목록 조회
```http
GET /api/proposals/{proposalId}/signatures?page={page}&size={size}
```

**파라미터**:
- `proposalId` (path): 제안 ID
- `page` (query, 기본값: 0): 페이지 번호
- `size` (query, 기본값: 10): 페이지 크기

**응답** (200 OK):
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "SUCCESS",
  "message": "요청에 성공하였습니다.",
  "result": {
    "content": [
      {
        "signatureId": 50,
        "userId": 1,
        "nickname": "익명사용자123",
        "signatureType": "AGREE",
        "content": "이 정책이 정말 필요합니다.",
        "createdAt": "2024-11-25T10:30:00"
      },
      // ... 더 많은 항목
    ],
    "totalElements": 32,
    "totalPages": 4,
    "number": 0,
    "size": 10,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

**설명**: 제안에 대한 서명(의견) 목록 조회
- **nickname**: 서명 작성 시점에 제공된 닉네임 (사용자 프로필 닉네임과 다를 수 있음)

---

## 🔐 인증/보안

### 현재 상태
- **인증**: 구현되지 않음 (사용자 ID를 Request 파라미터로 받음)
- **향후 계획**:
  - JWT 토큰 기반 인증
  - OAuth2 (카카오) 소셜 로그인
  - Spring Security 통합

### Swagger 설정
- **JWT Bearer Token**: 구성되어 있지만 활성화되지 않음
- 향후 구현 시 요청 헤더에 다음과 같이 포함:
  ```
  Authorization: Bearer {jwt_token}
  ```

---

## 🛑 에러 처리

### Exception Handling
- **글로벌 예외 처리**: `@ControllerAdvice` 사용 (`global/common/exception/ExceptionAdvice.java`)
- **커스텀 예외**: `RestApiException` 클래스 사용

### 에러 응답 형식
```json
{
  "timestamp": "2024-11-25T10:30:00",
  "code": "ERROR_CODE",
  "message": "에러 메시지",
  "result": null
}
```

---

## 🚀 프로젝트 실행 방법

### 1. 환경 설정

#### 필수 요구사항
- Java 17 이상
- MySQL 8.0
- Redis
- Docker & Docker Compose (선택)

#### 로컬 환경 설정
```bash
# 1. 프로젝트 클론
git clone <repository-url>

# 2. Docker Compose로 MySQL, Redis 실행
docker-compose -f docker-compose.local.yml --env-file .env.local up -d

# 3. IntelliJ로 프로젝트 열기
# File → Open → 프로젝트 폴더 선택

# 4. ProductApplication.java 실행
# Run → Run 'ProductApplication'
```

### 2. 접속 정보

- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **MySQL**: localhost:13306
- **Redis**: localhost:16379

### 3. 데이터베이스 초기화

- **DDL 자동 생성**: `ddl-auto: update` (application-local.yml)
- 기존 테이블과 호환 가능하게 업데이트됨

---

## 📋 설정 파일

### application-local.yml
```yaml
server:
  port: 80                    # 포트 번호 (기본 8080에서 80으로 변경 가능)

spring:
  datasource:
    url: jdbc:mysql://localhost:13306/minjulog_local
    username: mj
    password: mj*202412
  
  jpa:
    hibernate:
      ddl-auto: update       # 엔티티 스키마 자동 생성/업데이트

  data:
    redis:
      host: localhost
      port: 16379
      password: mjredis123!

jwt:
  secret: ...               # JWT 시크릿 키
  accessExpiration: 360000000
  refreshExpiration: 1209600000
```

---

## 🛠️ 주요 기술 상세

### 1. **Spring Data JPA + QueryDSL**
- JPA를 통한 ORM
- QueryDSL로 복잡한 쿼리 작성

### 2. **Redis**
- 세션 저장소 (향후 예정)
- Rate Limiting 데이터 저장

### 3. **Rate Limiting (Bucket4j)**
- 사용자별 API 호출 제한
- Whitelist 설정 가능 (예: Swagger UI)

### 4. **Swagger 3 (OpenAPI)**
- API 자동 문서화
- Swagger UI에서 API 테스트 가능

---

## 📝 개발 가이드

### Entity 생성 규칙
1. `BaseEntity` 상속 (자동으로 `createdAt`, `updatedAt` 포함)
2. `@NoArgsConstructor(access = AccessLevel.PROTECTED)` 필수
3. Builder 패턴은 생성자에만 적용 (전체 클래스에 적용 금지)

### 예외 처리 규칙
1. 커스텀 예외는 `RestApiException` 사용
2. `BaseCodeInterface`를 상속한 Enum으로 에러 코드 정의
3. `ExceptionAdvice`에서 전역 처리

### DTO/Converter 규칙
- Request DTO → Command 변환은 `Converter` 클래스에서 처리
- Response DTO는 UseCase Result를 이용해 생성

### UseCase 패턴
- 각 비즈니스 로직은 독립적인 UseCase 구현
- Command/Result 패턴 사용 (불변성 보장)

---

## 📚 추가 참고

### 민감 정보 관리
- `.env.local` 파일에서 환경 변수 관리
- `application-local.yml` (DB 연결 정보, API 키 등)
- **주의**: Git에 커밋하지 말 것!

### 테스트 코드
- 기본 CRUD 흐름 학습용 테스트 코드 제공
- 위치: `src/main/java/com/server/domain/test/`

### 향후 개선 사항
- [ ] Spring Security + JWT 인증 완성
- [ ] OAuth2 소셜 로그인 연동
- [ ] Flyway 마이그레이션 적용
- [ ] Facade 패턴 도입 (의존성 감소)
- [ ] 더 많은 테스트 코드 추가

---

## 🔗 참고 자료

- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [QueryDSL](http://www.querydsl.com/)
- [Swagger/OpenAPI](https://swagger.io/)
- [Bucket4j Rate Limiting](https://github.com/vladimir-bukhtoyarov/bucket4j)

## 📝 변경 이력

### v1.2 (2024-11-25)

#### 신규 기능
- **Topic 관리 API 추가**:
  - `GET /api/topics`: 토픽 목록 조회
  - `GET /api/topics/{id}`: 토픽 상세 조회  
  - `DELETE /api/topics/{id}`: 토픽 삭제

- **Proposal 생성/조회 시 Topic 연관관계 완성**:
  - Proposal 생성 시 `topicId` 필드로 토픽 자동 매핑
  - Proposal 상세/목록 조회 시 `topicId`, `topicName` 필드 포함

---

### v1.1 (2024-11-25)

#### 버그 수정
- **서명 개수 2개 이상 시 오류**: `GetProposalDetailService`에서 `null`을 비교하려던 문제 수정 (Comparator.nullsLast() 추가)

#### 신규 기능
- **Topic 엔티티 추가**: 제안을 토픽/카테고리별로 분류 가능
  - `POST /api/proposals` 요청 시 `topicId` 추가
  - 제안 상세/목록 조회에 `topicId`, `topicName` 필드 포함

#### API 개선
- **Proposal 목록 조회 강화**:
  - `body` 필드 추가
  - 검색 범위 확대 (제목 + 본문 + 해시태그)
  - `totalElements` 필터링된 결과의 개수로 수정
  - 인기순 정렬 (`popular`) 추가: (찬성 서명 + 찬성 투표) - (반대 서명 + 반대 투표)
  - 조회수순 정렬 (`views`) 추가

- **ProposalSignature 닉네임 기능**:
  - 서명 등록 시 요청 바디에서 `nickname` 필드 추가 (필수)
  - 중복 서명 시 기존 서명의 닉네임 업데이트
  - 서명 목록 조회 시 서명 작성 시점의 닉네임 반환

#### Entity 변경
- **ProposalSignature**: `nickname` 필드 추가 (DB 마이그레이션 필요)
- **Proposal**: `topic_id` FK 필드 추가 (DB 마이그레이션 필요)
- **Topic**: 신규 엔티티 (테이블: `topic`)
