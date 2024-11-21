<div align="center">

# 반려견 용품 쇼핑몰 - 호두몰2 Backend
###### ※React와 NodeJS 기반의 호두몰1을 React와 Spring Boot로 마이그레이션 <br><br><br>

</div>
<div align="center">

<img src="https://github.com/user-attachments/assets/a2afa5e9-4c6e-4a31-a5d8-37c6bd96c4f3" width=300 alt="호두몰 로고">


</div>

<div align="center"><strong>React와 Spring Boot를 사용하여 RESTful API를 구현한 반려견 용품 쇼핑몰 프로젝트입니다.</strong></div>

<div align="center"><strong><a href="https://github.com/hodooha/hodoomall-fe" target="_blank">Fontend로 이동</a></strong></div>

<br>

## &#128161; Overview<br>

<img src="https://github.com/user-attachments/assets/e9ffd768-1a4b-4163-b626-99e1ddfcbd35" alt="호두몰 메인">


<br>**📆 프로젝트 기간 : 2024.09 ~ 2024.11 / 1인**
<br><br>
## :page_with_curl: 기능

* 이메일주소 혹은 **구글 계정**으로 **회원가입**과 **로그인**을 할 수 있습니다.
* **카테고리별**로 상품을 조회할 수 있고 상품 이름으로 **검색**할 수 있습니다.
* 상품의 **상세 정보**를 조회할 수 있고, 사이즈를 선택하여 **카트**에 넣을 수 있습니다.
* 카트 페이지에서 **상품의 수량을 수정**할 수 있고 **주문**할 수 있습니다.
* 이벤트 페이지에서 **쿠폰**들을 **조회**하고 **다운**받을 수 있습니다.
* 마이페이지에서 **주문 내역**과 다운받은 **쿠폰 내역**을 **조회**할 수 있습니다.
* 관리자페이지에서 **전체 상품, 쿠폰, 주문 내역**을 **조회**할 수 있습니다.
* 관리자페이지에서 **상품, 쿠폰**을 **추가/수정/삭제**할 수 있습니다.

<br><br>
## 💻 개발환경
### 개발
**언어 : Java version 17**

**Frame work : Spring Boot(Backend), React(Frontend)**

**Security & Auth : Spring Security, JWT Authentication, Google OAuth**

**Tools : IntelliJ, Visual Studio Code, Docker**

### DB
**DB : MongoDB, Redis, RabbitMQ(Messaging)**

[![stackticon](https://firebasestorage.googleapis.com/v0/b/stackticon-81399.appspot.com/o/images%2F1732073018693?alt=media&token=099c896a-0267-4249-b9e3-0fcd17890cac)](https://github.com/msdio/stackticon) <br><br>

## &#128187; 적용 기술 및 주요 구현 기능  <br>
- [X] **Spring Security, JWT, OAuth2, Redis - 인증 및 권한 관리**<br>

* Spring Security, JWT를 사용하여 인증 및 인가를 구현했고, OAuth2로 구글 로그인을 추가했습니다.
* Redis 기반 Refresh Token 관리와 HttpOnly 쿠키를 활용하여 Replay Attack을 방지하고 인증 시스템의 보안을 강화했습니다.
* TTL을 설정하여 Redis에 저장된 만료된 토큰을 자동으로 삭제하고, Access Token 갱신 속도를 최적화하여 성능을 개선했습니다.
* Refresh Token Rotation(RTR)기법을 적용하여 탈취된 Refresh Token의 재사용을 차단하여 보안성을 강화했습니다.
<br><br>

- [x] **Redis, RabbitMQ - 선착순 쿠폰 발급**<br>

* 쿠폰 수량 관리를 Redis 캐시로 구현하고 Spring Scheduler로 DB와 동기화하여 응답시간을 단축하고 성능을 개선했습니다.
* 쿠폰 발행을 RabbitMQ 메시지큐를 사용하여 비동기 처리함으로써 동시성 충돌을 피하고 안정성을 높였습니다.

**[구현결과]**        
<table>
<thead>
  <tr>
    <th rowspan="2">구분</th>
    <th colspan="4">응답시간(m/s)</th>
    <th rowspan="2">처리량<br />(req/sec)</th>
  </tr>
  <tr>
    <th>평균</th>
    <th>최소</th>
    <th>최대</th>
    <th>표준편차</th>
  </tr>
</thead>
<tbody align="right">
  <tr>
    <td>기존</td>
    <td>501</td>
    <td>10</td>
    <td>4,973</td>
    <td>784.56</td>
    <td>125.3</td>
  </tr>
  <tr>
    <td>Redis</td>
    <td>125</td>
    <td>8</td>
    <td>355</td>
    <td>73.15</td>
    <td>430.7</td>
  </tr>
  <tr>
    <td>RabbitMQ</td>
    <td>210</td>
    <td>10</td>
    <td>560</td>
    <td>121.13</td>
    <td>315.3</td>
  </tr>
  <tr>
    <td>Redis + RabbitMQ</td>
    <td>181</td>
    <td>8</td>
    <td>712</td>
    <td>126.06</td>
    <td>340.8</td>
  </tr>
</tbody>
</table>

* Redis 캐싱 적용으로 평균 응답 시간을 501ms에서 125ms로 감소                  
* RabbitMQ를 통한 비동기 처리로 처리량을 125.3req/sec에서 340.8req/sec로 개선
* 캐싱만 적용했을 때 성능 수치는 가장 좋았지만, 메시지큐를 함께 사용하여 동시성 문제를 해결하고 안정성을 높이기 위해 __최종적으로 캐싱과 메시지 큐를 함께 적용__

<br>

- [x] **Docker - MongoDB 트랜잭션 처리를 위한 레플리카셋 구성**<br>
* MongoDB 트랜잭션 기능 활용을 위해 Docker를 사용하여 레플리카셋을 구성했습니다.
* Spring Boot MongoDB 트랜잭션으로 상품 주문 기능에서 원자성, 일관성, 독립성, 영속성을 보장했습니다.
<br><br>

- [x] **Redux Toolkit - 상태 및 데이터흐름 관리**<br>
* Redux Toolkit을 통해 상태 관리와 React 컴포넌트 간 데이터흐름을 효율적으로 구현했습니다.
* Redux Thunk를 사용하여 비동기 데이터 처리를 간소화하고 상태 변경에 따른 UI 업데이트를 최적화했습니다.


<br><br>
