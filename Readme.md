# GameHub-API
## 멀티모듈 프로젝트

## Apis 
### gamehub_auth_api
#### Member 인증/인가를 담당하는 실행가능한 api 모듈
- v1.0.2 cors 설정
- v1.0.3 RefreshToken Secure HttpOnly Cookie 설정
- v1.0.4 cors 재설정
- v1.0.5 가입 시 email null,blank validation 추가 

### gamehub_s3_api
#### S3 API 를 사용하여 Post 도메인과 버킷에 미디어를 저장하는 실행가능한 api 모듈
- v1.0.0 업로드/삭제
- v1.0.1 UpdateDto Tags validate 수정
- v1.0.2 thumbnail 추가로 인한 업로드 제한 개수 변경 5->6, 허용 파일 형식 수정

### gamehub_post_api
#### Post 도메인을 사용하여 서비스를 제공하는 실행가능한 api 모듈
- v1.0.0 Post 읽기, 수정, 유저 Feed, 최근 업로드 보기

## Domains 
### token_domain
#### token 도메인 모듈
### member_domain 
#### member 도메인 모듈 
### post_domain
#### post 도메인 모듈

## Core
#### api 모듈에서 공통적으로 사용되는 모듈
