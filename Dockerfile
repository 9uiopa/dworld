# 빌드 단계----------------------------------
FROM gradle:7.5.1-jdk17 AS build

# 작업 디렉토리 설정 (컨테이너 경로)
WORKDIR /app

# Gradle 캐시를 활용하기 위해 build.gradle, settings.gradle, gradlew 을 (컨테이너의) /app으로 복사
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Gradle 종속성 캐시 빌드
RUN ./gradlew build --no-daemon || return true

# 소스 코드 복사 (호스트의 src 폴더내의 모든 파일을 컨테이너의 app/src로 복사)
COPY src ./src

# 애플리케이션 빌드 실행
RUN ./gradlew clean bootJar --no-daemon

# 실제 런타임 이미지--------------------------------------
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드(build - 전단계에서 AS build로 정의) 단계에서 생성된 JAR 파일 복사 (컨테이너쪽 jar 파일 -> 컨테이너 app 내에 .jar로 복사)
COPY --from=build /app/build/libs/dworld.jar ./dworld.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행 명령 설정
ENTRYPOINT ["java", "-jar", "dworld.jar"]