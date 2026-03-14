# AI Chatbot Backend
AI 기반 채팅 백엔드 과제 구현물입니다. 사용자, 스레드(대화), 메시지를 관리하고, 외부 AI 키가 없어도 동작하도록 설계했습니다. JWT 인증, 권한별 접근 제어, 대화 맥락 기반 응답 흐름을 단순하고 명확하게 구현했습니다.

## 1. 과제 개요
- AI 기반 채팅 백엔드 과제.
- 사용자, 대화 스레드, 메시지를 관리하고 AI 응답을 생성/저장하는 API 제공.
- 단순 CRUD가 아니라 “대화 맥락을 관리하는 백엔드”로 해석해 설계.

## 2. 과제 분석
- 핵심 요구사항: 사용자별 대화 관리, 대화 이력 저장, AI 응답 생성, API 제공.
- 스레드(ChatThread)와 메시지(Chat)로 분리해 맥락을 유지하고, 사용자 메시지·AI 메시지를 모두 저장.
- AI API Key 미지급 조건을 고려해, AI 응답 생성 책임을 서비스 계층으로 분리하고 키 없음/호출 실패 시 fallback 응답을 반환하도록 설계.
- 외부 AI와 강결합하지 않고, 연동 유무에 따라 교체 가능한 구조를 지향.

## 3. 구현 방향 및 설계 판단
- 레이어드 아키텍처: Controller / Service / Repository로 역할 분리.
- 스레드(ChatThread)와 메시지(Chat) 분리: 대화 이력 확장성, 스레드 단위 조회·삭제·집계 단순화.
- PK 타입(Long) 선택: 숫자형이 인덱싱/정렬/조인에 유리하고 외부 노출 ID가 불필요해 단순한 내부 PK로 사용.
- 시간 컬럼(createdAt 등): 정렬, 최근 24시간 집계, 보고서 생성에 활용.
- 메시지 role(user/assistant) 분리: 대화 재구성·로그 분석 시 명확성 확보.
- AI 응답 서비스 분리: 키 미설정/예외/파싱 실패 시 기본 응답을 반환해 부팅·요청 흐름이 끊기지 않도록 함.
- 과제 범위에서 “단순·명확”을 우선하고 과도한 추상화는 지양.

## 4. 기술 스택
- Kotlin, Spring Boot (Web, Security, Validation)
- Spring Data JPA
- PostgreSQL
- Gradle (Kotlin DSL)
- Kotlin data class 활용, JPA 엔티티는 필요한 곳만 `open` 처리

## 5. 프로젝트 구조
```
src/main/kotlin/com/mook/aichatbot
  auth/         인증, JWT, 로그인 기록
  user/         사용자 도메인, 리포지토리
  thread/       대화 스레드 도메인, 서비스
  chat/         채팅 메시지 도메인, 서비스
  feedback/     피드백 도메인, 서비스
  analytics/    관리자 집계·리포트
  common/config 보안·설정
  ai/           OpenAI 연동 래퍼(키 없어도 동작하도록 분리)
```
- Controller: HTTP 엔드포인트
- Service: 비즈니스 로직, 권한 체크, 트랜잭션
- Repository: JPA 기반 영속성 계층
- Domain: 엔티티 및 enum
- Config: Security/Properties 설정

## 6. 주요 기능
- 회원가입/로그인, JWT 인증
- 질문 생성: 30분 규칙 기반 스레드 재사용, 맥락을 붙여 AI 응답 생성 후 저장 (키 없으면 fallback)
- 대화 목록 조회, 스레드 삭제(soft delete)
- 피드백 생성/조회/상태 변경(ADMIN)
- 관리자 집계(최근 24시간 가입/로그인/대화 수), 관리자 CSV 리포트(최근 24시간 대화)

## 7. API 설계
| Method | URI | Description |
| --- | --- | --- |
| POST | /api/auth/signup | 회원가입 |
| POST | /api/auth/login | 로그인 |
| POST | /api/chats | 질문 생성(스레드 자동 생성/재사용) |
| GET  | /api/chats | 스레드 단위 대화 목록 조회(페이징, 정렬) |
| DELETE | /api/threads/{threadId} | 스레드 soft delete |
| POST | /api/feedbacks | 피드백 생성(자신의 대화만, ADMIN은 전체) |
| GET  | /api/feedbacks | 피드백 목록 조회(필터/정렬/페이징) |
| PATCH | /api/admin/feedbacks/{feedbackId}/status | 피드백 상태 변경(ADMIN) |
| GET | /api/admin/analytics/activity | 최근 24시간 활동 집계(ADMIN) |
| GET | /api/admin/reports/chats | 최근 24시간 대화 CSV 다운로드(ADMIN) |

요청/응답 예시 (질문 생성)
```json
POST /api/chats
{
  "question": "안녕! 오늘 날씨 어때?",
  "isStreaming": false
}
```
```json
{
  "threadId": 12,
  "chatId": 45,
  "question": "안녕! 오늘 날씨 어때?",
  "answer": "OpenAI API Key가 설정되지 않아 실제 AI 응답 대신 기본 응답을 반환합니다.",
  "model": "gpt-4o-mini",
  "isStreaming": false,
  "createdAt": "2026-03-14T12:34:56"
}
```
- 흐름: 사용자 메시지 저장 → AI 서비스 호출(키 없으면 fallback) → assistant 응답 저장 → 응답 반환.

## 8. 데이터 모델 / 설계
- User: id(Long), email, password, name, role, createdAt
- ChatThread: id(Long), userId, lastQuestionAt, createdAt, deletedAt(nullable)
- Chat: id(Long), threadId, userId, question, answer, model, isStreaming, createdAt
- Feedback: id, userId, chatId, isPositive, status, createdAt
- LoginHistory: id, userId, createdAt
- 관계: User 1:N ChatThread, ChatThread 1:N Chat, Chat 1:N Feedback(사용자·대화 복합 유니크로 1개 제한)
- Long PK 선택: 단순·효율적 인덱싱/조인, 외부 노출 ID 불필요
- role(user/assistant) 분리: 대화 재구성·로그 분석 용이
- createdAt 유지: 정렬, 최근 24시간 집계, 리포트 생성에 필요

## 9. AI 활용
- 요구사항 해석, API/엔티티 구조 초안 검토 시 AI를 보조 도구로 활용.
- Kotlin/Spring Boot 코드 스니펫, DTO/예외 처리, README 문안 정리에서 아이디어 참고.
- 최종 설계와 코드 검증은 직접 수행해 일관성과 요구사항 부합 여부를 확인.

## 10. 구현하면서 어려웠던 점
- 문제: AI 키 없이도 서비스가 죽지 않으면서, 실제 연동 구조는 유지해야 함.
- 고민: 외부 API 호출 실패 시 전체 플로우 중단을 막고, fallback 응답을 저장·반환해야 함.
- 해결: AI 호출을 별도 서비스로 분리하고, 키 미설정/예외/파싱 실패 시 기본 응답을 반환하도록 안전 가드 추가. 이를 통해 부팅·요청 흐름을 안정화했고, 실서비스 연동 시 동일 인터페이스 교체만으로 확장 가능.
- 배운 점: 외부 연동 불안정성을 도메인 로직과 분리하면 운영 안정성과 테스트 용이성이 크게 높아진다.

## 11. 실행 방법
1) PostgreSQL 준비 및 DB 생성
```sql
CREATE DATABASE aichatbot;
```
2) 환경 변수 또는 `src/main/resources/application.yml` 설정
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/aichatbot
    username: postgres
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
openai:
  api-key: ${OPENAI_API_KEY:}   # 없으면 빈 값, fallback 응답 사용
  base-url: https://api.openai.com/v1
  model: gpt-4o-mini
```
3) 빌드 및 실행
```bash
./gradlew bootRun
```
4) 기본 주소
- http://localhost:8080

`OPENAI_API_KEY` 미설정 시에도 fallback 응답으로 정상 동작하며, 키를 세팅하면 동일 인터페이스로 실제 연동 가능합니다.
