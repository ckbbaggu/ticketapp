#FROM eclipse-temurin:25-jdk
#WORKDIR /app
#COPY target/*.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","app.jar"]

# ---------- Stage 1 : Build ----------

FROM maven:3.9.11-eclipse-temurin-25 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ---------- Stage 2 : Runtime ----------

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
