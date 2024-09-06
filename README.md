# 당장 Dangjang Android app

[당장 - 당뇨 관리, 혈당, 식단, 기록, 체중, 운동 - Google Play 앱](https://play.google.com/store/apps/details?id=com.dangjang.android&hl=ko-KR)

![당장 소개](https://github.com/user-attachments/assets/ffcbd439-2a57-4e77-a5a0-5e18ce001607)

소프트웨어 마에스트로 14기 프로젝트로, 당뇨를 편리하게 기록하고 개선할 수 있도록 가이드를 제공하는 서비스입니다.

혈당, 체중, 운동, 식단 관리에 대한 건강 가이드를 통해 사용자의 생활 습관 관리를 돕고, 당뇨 관리를 조금 더 편하게 할 수 있는 사회를 만들고자 합니다.

## 📱 주요 기능 소개

![당장 주요 기능](https://github.com/user-attachments/assets/d9560fa3-d1da-4c8d-889a-f4d1bf16f743)

► 건강 데이터 자동 기록

Google Health Connect 연동을 통해 스마트 워치, 삼성 헬스, 디바이스 내 건강 데이터(혈당, 체중, 운동, 칼로리)를 자동으로 불러옵니다.

► 맞춤 건강 가이드 제공

내분비내과 교수와 협업한 내용을 바탕으로 기록된 건강 데이터에 대해 가이드를 제공합니다.

► 건강 차트

주 단위의 건강 데이터 변화를 차트로 시각화해서 제공합니다. 혈당, 체중, 운동, 칼로리 변화 추이를 한 눈에 확인할 수 있습니다.

► 포인트 제공

당뇨를 관리하며 포인트를 얻을 수 있고, 쌓인 포인트로 기프티콘 교환이 가능합니다.

## 📈 서비스 성과

![서비스 성과](https://github.com/user-attachments/assets/22f69f79-35df-4ccd-ba45-faf9981a3c0a)

DAU 10명 , WAU 220명 , MAU 570명 성과를 달성하였습니다.

## 🏗️ Android Architecture
![안드로이드 아키텍처](https://github.com/user-attachments/assets/c772e7a5-df0e-494b-8888-bcb7d1c18b90)

### Multi Module을 통한 Clean Architecture

- data, domain, presentation, common-ui 모듈 분리를 통해 프로젝트 유지 보수성을 높여 개발 속도를 향상시켰습니다.
- 멀티 모듈에 대응하기 위해 Hilt를 통한 DI를 사용했습니다.
- DTO, VO를 분리하여 백엔드에서 내려주는 값을 한번 더 검증함으로써 안정성을 높였습니다.
- Flow를 사용해 백엔드에서 내려온 data를 view까지 도달시키는 과정을 구현했습니다.

## ⚒️ 사용 기술

- MVVM, Clean Architecture, Multi Module, Hilt
- Coroutine, Flow
- Databinding, ViewModel, Navigation
- OkHttp3, Retrofit2, Google Health Connect API, FCM, Kakao/Naver Login
- Language : Kotlin
