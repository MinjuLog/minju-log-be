
## 프로젝트 세팅 관련
> 📝 README 파일에 원래 이런 내용은 잘 적지 않지만,  
> 팀원들이 참고할 수 있도록 임시로 작성했습니다.  
> (나중에는 삭제 예정)
> 시간이 없어서 동작에 대해 테스트는 하지 못했습니다.
> 읽어 보시고 프로젝트 세팅에 대해 수정하면 좋을 부분을 말씀해주세요.

테스트 코드를 기본적인 CRUD 흐름을 학습하기 위한 예제처럼 작성해봤습니다.  
스프링을 처음 접하는 사람도 Controller → Service → Repository 구조와  
DTO / Entity / Mapper 역할을 쉽게 이해할 수 있도록 작성되었습니다.

**프로젝트 실행 방법**
1. 프로젝트 클론
2. IntelliJ로 열기
3. docker-compose.local.yml을 env 파일과 실행
4. Application 실행
5. localhost:8080/swagger-ui/index.html 접속
6. API 테스트

## 민감 정보 관리 관련 안내

민감 파일(application.yml, .env 파일 등) 관리를 서브묘듈을 통해 진행할지 아니면
여러분이 선호하는 다른 방법(ex. 노션에 적어서 관리 등)이 있는지 고민해보시고 결정하시면 됩니다.

*민감 파일은 나중에 공유해드리겠습니다.*

1. **Git Submodule 방식**
    - 민감 설정 파일 전용 레포지토리를 따로 만들어 서브모듈로 연동
    - 장점: 버전 관리 용이, 환경별 분리 쉬움
      - 단점: 세팅이 약간 번거로움

2. **외부 공유 방식 (예: Notion, Google Drive 등)**
    - 단순 텍스트로 관리하고 필요 시 직접 내려받아 사용
    - 장점: 간단하고 접근성 높음
    - 단점: 변경 이력 추적 어려움

## 에러 처리 관련
전역적인 에러 처리는 `@ControllerAdvice`를 활용하여 전역적으로 관리하고 있습니다.
- `global/common/exception/ExceptionAdvice.java`

## 예외처리 관련
커스텀 예외 클래스를 사용합니다.
- `global/common/exception/RestApiException.java`

예외 처리 시에는 `RestApiException`에 `BaseCodeInterface`를 상속받은 `ENUM`으로 만들어진 에러 코드를 넘겨주면 됩니다.

## 엔티티 생성 관련
엔티티 생성 시에는 `BaseEntity`를 상속받아 생성합니다.
- `global/common/entity/BaseEntity.java`
빌더 패턴 사용 시, Entity 전체에다 붙이는게 아닌 생성자에만 붙여서 생성하도록 합니다. (바뀌면 안되는 값 바뀌는 것 방지)


## 프로젝트 구조 관련
현재 구조는 아래와 같습니다.
```
src
 ┣ main
 ┃ ┣ java
 ┃ ┃ ┗ com.example.demo
 ┃ ┃   ┣ controller
 ┃ ┃   ┣ service
 ┃ ┃   ┣ repository
 ┃ ┃   ┣ dto
 ┃ ┃   ┣ entity
 ┃ ┃   ┗ mapper
```

각 DDD 레이어의 역할은 다음과 같습니다.
- Controller: HTTP 요청을 처리하고, Service 레이어를 호출합니다.
- Service: 비즈니스 로직을 처리하고, Repository 레이어를 호출합니다.
- Repository: 데이터베이스와의 상호작용을 담당합니다.
- DTO: 데이터 전송 객체로, 프론트와 데이터 교환을 위해 사용됩니다.
- Entity: 데이터베이스 테이블과 매핑되는 객체입니다.
- Mapper: DTO와 Entity 간의 변환을 담당합니다.

해당 구조튼 다른 도메인의 서비스를 참조할 수 밖에 없는 구조입니다.
예를 들어, UserService에서 OrderService를 참조하는 경우가 있을 수 있습니다.

좀 더 상호의 의존성을 줄이기 위해
레이어를 하나 더 추가하는 방법도 있습니다.

```
src
 ┣ main
 ┃ ┣ java
 ┃ ┃ ┗ com.example.demo
 ┃ ┃   ┣ controller
 ┃ ┃   ┣ service
 ┃ ┃   ┣ repository
 ┃ ┃   ┣ dto
 ┃ ┃   ┣ entity
 ┃ ┃   ┣ mapper
 ┃ ┃   ┗ facade
```
- Facade: 여러 서비스의 기능을 조합하여 제공하는 레이어입니다.
- Controller는 Facade를 호출하고, Facade는 여러 Service를 호출합니다.
- Service는 Repository만 호출합니다.

이렇게 하면 Controller와 Service 간의 의존성을 줄일 수 있습니다.
하지만 복잡도가 증가합니다.

