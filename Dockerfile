#Stage 1: Build JAR
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true && ls -l /app/target

# Stage 2: Run JAR
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# คัดลอก jar ที่ build ได้มา ไม่ต้อง fix ชื่อ
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]