# 1. Gradle 빌드를 위한 이미지 (Java 17과 Gradle 설치)
FROM gradle:7.6-jdk17 AS build

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 프로젝트 파일 복사
COPY . .

# 4. Gradle 빌드 실행 (테스트 제외)
RUN ./gradlew build -x test

# 5. 실제 실행할 이미지 (JAR 파일을 실행할 OpenJDK 이미지)
FROM openjdk:17-jdk

# 6. JAR_FILE 변수 정의 (빌드된 JAR 파일의 경로 지정)
ARG JAR_FILE=./build/libs/klkl-0.0.1-SNAPSHOT.jar

# 7. 빌드된 JAR 파일을 실행 환경으로 복사
COPY --from=build /app/build/libs/klkl-0.0.1-SNAPSHOT.jar /app.jar

# 8. 시스템 진입점 정의 (JAR 파일 실행)
ENTRYPOINT ["java", "-jar", "/app.jar"]
