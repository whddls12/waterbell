

<div align=center>
![image__3_-removebg-preview__2_](/uploads/da1d08b75b69a3ef6a512dad0ca862b7/image__3_-removebg-preview__2_.png)
 <h1>지하차도, 지하주차장 침수관리 시스템</h1>
</div>

<br>

# :star: 소개

## :boy: 팀원

| [김동현](https://github.com/DongHyun-Klm) | [김예진]() | [황종인]() | [황윤영]() | [이효경]() | [조준희](https://github.com/jjunehee) |
| :------------------------------------------: | :------------------------------------------: | :-------------------------------------: | :------------------------------------: | :-----------------------------------: | :-----------------------------------: |
|       ![김동현](./img/김동현.png)       |       ![김예진](./img/김예진.png)         |      ![황종인](./img/황종인.png)       |      ![황윤영](./img/황윤영.png)      |         ![이효경](./img/이효경.png)            |         ![조준희](./img/조준희.png)          |
|                BackEnd<br>FrontEnd                |                IoT<br>BackEnd                |            FrontEnd            |          FrontEnd<br>BackEnd           |           BackEnd<br>Infra            |           IoT<br>BackEnd            |

<br>



## :sparkles: 프로젝트 기획 배경
<h3> 여름철 폭우로 인해 전국적으로 침수 피해가 끊이지 않고 있다. <br>앞으로 '장마'가 아닌 '우기'로 단어를 변경해야 한다는 주장도 나오고 있는 가운데, 정부에서도 침수 예방 대책을 구축, 개선 하려고 한다. </h3>

<br>

## :balloon: 개요
### *_🖐 SSAFY 9기 2학기 공통 프로젝트 🖐_**  
<h3>"세상의 모든 침수에 대응한다." 침수를 IoT 장비를 통해 감지하고 침수 피해를 예방 및 방지하는 서비스 </h3>

> 2023.07.10 ~ 2023.08.18 (6주)

<br>


## :dart: WaterBell 기능 설명

### [IoT] 수위 측정
- 현장에 위치한 IoT는 부표와 적외선 센서를 이용하여 실시간 수위를 측정합니다.
- 측정된 값은 웹 서비스에 전송됩니다.

### [IoT] 현장 영상 촬영
- 현장에 위치한 IoT는 지하차도, 지하주차장 실시간 영상을 촬영합니다.
- 촬영된 영상은 웹 서비스에 전송됩니다.

### [Web] 현황판 데이터 조회
- (관리자, 사용자)는 메인, 제어페이지에서 현장 실시간 CCTV 화면을 확인합니다.
- (관리자, 사용자)는 메인 페이지에서 날씨 API를 활용한 침수지도와 강수량 그래프를 조회합니다.
- (관리자, 사용자)는 메인 페이지에서 실시간 수위 그래프를 조회합니다.

### [Web] 센서 로그 조회
- 관리자는 실시간 로그(수위, 기기 상태 및 제어, 알림)를 조회합니다.
- 수위 값이 관리자가 설정해놓은 1차, 2차 기준치가 되면 사용자에게 웹 알림, 문자가 자동으로 전송됩니다.  

### [Web] 원격 제어
- 관리자는 1,2차 침수에 따라 IoT 기기(차수판, 전광판, LED)를 제어합니다.

### [Web] 신고 접수
- 사용자는 신고 접수글 작성을 통해 관리자, 사용자에게 사실을 전파합니다.

### [Web] 마이페이지
- 관리자는 마이페이지에서 시설의 1,2차 침수 기준치와 알림 메세지를 설정합니다.

<br><br>


# :rocket: 기능 시연

### IoT

| IoT 수위 측정 |    차수판 제어  |전광판 제어|
| :--------------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------: |
| ![KakaoTalk_20231110_010745028](/uploads/fe543ca93347fc1abe372d0ec8583545/KakaoTalk_20231110_010745028.mp4)  |                             ![KakaoTalk_20231108_211421914](/uploads/beb3ec97f9d6cc23d2d70e34f4d1984a/KakaoTalk_20231108_211421914.mp4)                              |               ![KakaoTalk_20231108_211414421](/uploads/aa26101911b9139b6a379c063ae280b1/KakaoTalk_20231108_211414421.mp4)               |

### 현황판

|             CCTV, 침수지도, 강수량, 수위 조회        |                          강수량 그래프                                          |                           수위센서 그래프                            |
| :--------------------------: | :--------------------------------------------------------------------------------------: | :-------------------------------------------------------------: |
| ![현황판 전체](/README.assets/대시보드 전체.gif)  |                             ![강수량 그래프](/README.assets/강수량 그래프.png)                              |               ![수위센서 그래프](/README.assets/수위센서 그래프.png)               |

### 제어, 알림

|            차수판, 전광판, LED 원격 제어            |                                  문자 알림                                           |                           웹 알림                            |
| :--------------------------: | :--------------------------------------------------------------------------------------: | :-------------------------------------------------------------: |
| ![ezgif.com-video-to-gif__4_](/uploads/0f6871d6c2c24a7fea60bc898112a817/ezgif.com-video-to-gif__4_.gif)  |                             ![차수판 작동문자](/README.assets/차수판 작동시 문자.png)                            |               ![ezgif.com-video-to-gif__3_](/uploads/41d532214e49f8db55b6090ab9506d87/ezgif.com-video-to-gif__3_.gif)               |

### 센서 내역
|  수위 센서    |     기기 상태      |       기기 제어         |       알림 로그         |
| :----------------------: | :---------------------------------------: | :--------------------------------------: |:--------------------------------------: |
| ![Untitled__3_](/uploads/7a2a8d815736d376961a3b8a072b1718/Untitled__3_.png)  |   ![Untitled__5_](/uploads/a9466c3d5fe6fd87339a14a5840e77b1/Untitled__5_.png)       | ![Untitled__6_](/uploads/e6291b7bb4038820bbfa6ad84ec86472/Untitled__6_.png)    |![Untitled__4_](/uploads/a292c933d3dbca695d9de3fd6cc94fa1/Untitled__4_.png)    |


### 신고접수

|  신고접수 글 등록시 웹알림   |  신고접수글 조회   |
| :--------------------------: | :--------------------------: |
| ![신고접수 등록알림](/README.assets/신고접수등록알림.png) | ![신고접수 조회 및 등록](/README.assets/신고접수페이지.png) |

|  신고접수 작성    |     신고접수 상세      |       신고접수 수정         |
| :----------------------: | :---------------------------------------: | :--------------------------------------: |
| ![신고접수 작성](/README.assets/신고접수작성.png)  |     ![신고접수 상세](/README.assets/신고접수상세.png)    | ![신고접수 수정](/README.assets/신고접수수정.png)    |

### 회원가입, 로그인

|  일반 회원가입    |     소셜 회원가입1      |       소셜 회원가입2         |
| :----------------------: | :---------------------------------------: | :--------------------------------------: |
| ![일반 회원가입](/README.assets/회원가입.png)  |   ![소셜 로그인1](/README.assets/소셜로그인1.png)       | ![소셜 로그인2](/README.assets/소셜로그인2.png)    |

|       소셜 회원가입3         |       소셜 회원가입4         |
| :---------------------------------------: | :--------------------------------------: |
| ![소셜 로그인3](/README.assets/소셜로그인3.png)    | ![소셜 로그인4](/README.assets/소셜로그인4.png)    |

<br><br>

# :eyes: 산출물

### 기능 명세
![기능명세1](./img/기능명세1.PNG)
![기능명세2](./img/기능명세2.PNG)
![기능명세3](./img/기능명세3.PNG)
![기능명세4](./img/기능명세4.PNG)

<br>

### 아키텍처, 기술스택
![기술스택](/uploads/bf519b33320d569e62c4279dba1be5b3/기술스택.png)

<br>

### API 명세
![API명세1](./img/API명세1.PNG)
![API명세2](./img/API명세2.PNG)
![API명세3](./img/API명세3.PNG)
![API명세4](./img/API명세4.PNG)

<br>

### ERD
![ERD](/uploads/cbdb821af5066702a4d628eb18c756cc/ERD.PNG)

<br>

### IoT 통신 흐름도
![cctv](img/CCTVstructure.png)
![sensor](img/SensorStructure.png)

<br>

### 번다운 차트

|1주차|2주차|3주차|
|:----------------:|:----------------:|:--------------:|
| ![1주차](./img/1주차.PNG) | ![2주차](./img/2주차.PNG) | ![3주차](./img/3주차.PNG) |

|4주차|5주차|
|:----------------:|:----------------:|
| ![4주차](./img/4주차.PNG) | ![5주차](./img/5주차.PNG) |




