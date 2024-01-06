## 나비장터

> 물물교환의 장이 되는 곳 - 나비장터
> 

## 📣 프로젝트 목적

> 시장가치가 떨어진 물건을 비슷한 가치의 물건과 교환하는 과정을 쉽게 만들어주는 서비스입니다.
> 

## 🧐 팀원소개

| Tech Leader | Scrum Master | Developer |
| --- | --- | --- |
| https://github.com/hi-june | https://github.com/BeommoKoo-dev | https://github.com/born-A |
| <img src="https://avatars.githubusercontent.com/u/98803599?v=4" width="300" /> | <img src="https://avatars.githubusercontent.com/u/95630007?v=4" width="300" /> | <img src="https://avatars.githubusercontent.com/u/93516595?v=4" width="300" /> |

## 🛠 기술스택

### 개발 환경

<img src="https://img.shields.io/badge/Java17-007396?style=flat-square&logo=Java&logoColor=white&style=flat"/></a>
<img src="https://img.shields.io/badge/Spring Boot 2.7.17-6DB33F?style=flat-square&logo=Spring&logoColor=white&style=flat"/></a>
<img src="https://img.shields.io/badge/-Spring Data JPA-gray?logoColor=white&style=flat"/></a>
<img src="https://img.shields.io/badge/Query DSL-0078D4?style=flat-square&logo=Spring Data JPA&logoColor=white&style=flat"/></a>
<img src="https://img.shields.io/badge/MySQL 8-4479A1?style=flat-square&logo=MySQL&logoColor=white&style=flat"/></a>
<img src="https://img.shields.io/badge/Gradle-4429A1?style=flat-square&logoColor=white&style=flat"/></a>
<img src="https://img.shields.io/badge/Junit-25A162?style=flat-&logo=JUnit5&logoColor=white&style=flat"/></a>
<img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=flat-square&logo=amazon-aws&logoColor=white&style=flat"/>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=spring-security&logoColor=white&style=flat"/></a>

<img src="[https://img.shields.io/badge/](https://img.shields.io/badge/Spring)Redis-DC382D?style=flat-square&logo=redis&logoColor=white&style=flat"/></a>

<img src="[https://img.shields.io/badge/](https://img.shields.io/badge/Spring)Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=white&style=flat"/></a>

### 협업 툴

<img src="https://img.shields.io/badge/Notion-FFFFFF?style=flat-square&logo=Notion&logoColor=black"/></a>
<img src="https://img.shields.io/badge/slack-232F3E?style=flat-square&logo=slack&logoColor=white&style=flat"/></a>
<img src="https://img.shields.io/badge/Github-000000?style=flat-square&logo=Github&logoColor=white&style=flat"/></a>
<img src="[https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=Jira software&logoColor=white&style=flat](https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=Jira%20software&logoColor=white&style=flat)"/></a>

### 기타

<img src="https://img.shields.io/badge/IntelliJ IDEA-8A3391?style=flat-square&logo=IntelliJ IDEA&logoColor=black&style=flat"/></a>
<img src="https://img.shields.io/badge/ERDCloud-4429A7?style=flat-square&logoColor=white&style=flat"/></a>
<img src="[https://img.shields.io/badge/S](https://img.shields.io/badge/REST)wagger-#85EA2D?style=flat-square&logo=swagger&logoColor=white&style=flat">

## 📋 프로젝트 아키텍처

<p align="center">
<img src="https://github.com/team-nabi/nabi-market-server/assets/95630007/9cd4bfaa-4402-47a9-9e62-48d7c14355f5" width="900" height="400"/>
</p>

## 📋 ERD 다이어 그램

<p align="center">
<img src="https://github.com/team-nabi/nabi-market-server/assets/95630007/519ba641-6283-4328-918b-c5cae5ae9085" width="900" height="400"/>
</p>

## 📁 패키지 구조

- `global` : 도메인 전체에 적용되는 base entity, config, security 등을 담고있는 패키지.
- `domain` : 각 도메인의 root가 되는 패키지 .
- `api` : 레이어 아키텍쳐 중 컨트롤러가 위치한 패키지.
- `dto` : 계층간 통신을 위한 dto가 담긴 패키지.
- `service` : 레이어 아키텍쳐 중 서비스가 위치한 패키지.
- `entity` : JPA 엔티티 클래스, Enum 등이 들어간 패키지.
- `domain` : 각 도메인 엔티티와 레포지토리, 도메인에 필요한 enum들을 갖고 있는 패키지.
<details>
<summary><h4> 📌 상세 보기</h4></summary>

```bash
.
├── main
│   ├── java
│   │   └── org
│   │       └── prgrms
│   │           └── nabimarketbe
│   │               ├── domain
│   │               │   ├── card
│   │               │   │   ├── api
│   │               │   │   ├── dto
│   │               │   │   │   ├── request
│   │               │   │   │   └── response
│   │               │   │   │       ├── projection
│   │               │   │   │       └── wrapper
│   │               │   │   ├── entity
│   │               │   │   ├── repository
│   │               │   │   └── service
│   │               │   ├── cardimage
│   │               │   │   ├── dto
│   │               │   │   │   ├── request
│   │               │   │   │   └── response
│   │               │   │   ├── entity
│   │               │   │   └── repository
│   │               │   ├── category
│   │               │   │   ├── entity
│   │               │   │   └── repository
│   │               │   ├── chatroom
│   │               │   │   ├── api
│   │               │   │   ├── dto
│   │               │   │   │   └── response
│   │               │   │   │       ├── list
│   │               │   │   │       └── single
│   │               │   │   ├── entity
│   │               │   │   ├── repository
│   │               │   │   └── service
│   │               │   ├── completeRequest
│   │               │   │   ├── api
│   │               │   │   ├── dto
│   │               │   │   │   ├── request
│   │               │   │   │   └── response
│   │               │   │   │       ├── projection
│   │               │   │   │       └── wrapper
│   │               │   │   ├── entity
│   │               │   │   ├── repository
│   │               │   │   └── service
│   │               │   ├── dibs
│   │               │   │   ├── api
│   │               │   │   ├── dto
│   │               │   │   │   └── response
│   │               │   │   │       ├── projection
│   │               │   │   │       └── wrapper
│   │               │   │   ├── entity
│   │               │   │   ├── repository
│   │               │   │   └── service
│   │               │   ├── item
│   │               │   │   ├── entity
│   │               │   │   └── repository
│   │               │   ├── notifiaction
│   │               │   │   ├── api
│   │               │   │   ├── dto
│   │               │   │   │   ├── request
│   │               │   │   │   └── response
│   │               │   │   │       ├── projection
│   │               │   │   │       └── wrapper
│   │               │   │   ├── entity
│   │               │   │   ├── repository
│   │               │   │   └── service
│   │               │   ├── oauth2
│   │               │   │   ├── google
│   │               │   │   │   ├── api
│   │               │   │   │   ├── domain
│   │               │   │   │   ├── dto
│   │               │   │   │   └── service
│   │               │   │   └── kakao
│   │               │   │       ├── api
│   │               │   │       ├── dto
│   │               │   │       └── service
│   │               │   ├── suggestion
│   │               │   │   ├── api
│   │               │   │   ├── dto
│   │               │   │   │   ├── request
│   │               │   │   │   └── response
│   │               │   │   │       └── projection
│   │               │   │   ├── entity
│   │               │   │   ├── repository
│   │               │   │   └── service
│   │               │   └── user
│   │               │       ├── api
│   │               │       ├── dto
│   │               │       │   ├── request
│   │               │       │   └── response
│   │               │       ├── entity
│   │               │       ├── repository
│   │               │       └── service
│   │               └── global
│   │                   ├── annotation
│   │                   ├── aws
│   │                   │   ├── api
│   │                   │   └── service
│   │                   ├── config
│   │                   ├── error
│   │                   ├── event
│   │                   ├── redisson
│   │                   ├── security
│   │                   │   ├── entity
│   │                   │   ├── handler
│   │                   │   └── jwt
│   │                   │       ├── dto
│   │                   │       ├── filter
│   │                   │       ├── provider
│   │                   │       └── repository
│   │                   └── util
│   │                       └── model
│   └── resources
│       ├── firebase
│       └── static
└── test
    ├── java
    │   └── org
    │       └── prgrms
    │           └── nabimarketbe
    │               ├── config
    │               ├── domain
    │               │   ├── card
    │               │   │   ├── repository
    │               │   │   └── service
    │               │   ├── dibs
    │               │   │   └── repository
    │               │   └── user
    │               │       └── service
    │               └── setup
    │                   ├── jwt
    │                   ├── oauth2
    │                   │   └── request
    │                   └── user
    │                       └── request
    └── resources
```
