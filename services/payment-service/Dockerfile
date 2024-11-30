# Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -f pom.xml clean package -Dmaven.test.skip

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8036
ENTRYPOINT ["java","-jar","app.jar"]