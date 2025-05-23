# Build stage
FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build generateJavadoc -x test

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/JobPortalJosephSfeir-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/build/docs/javadoc /app/javadoc
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]