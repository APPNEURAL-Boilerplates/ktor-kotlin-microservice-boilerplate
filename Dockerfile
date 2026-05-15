FROM gradle:9-jdk21 AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle settings.gradle.kts build.gradle.kts gradle.properties ./
COPY --chown=gradle:gradle src ./src
RUN gradle clean installDist --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /home/gradle/project/build/install/ktor-kotlin-microservice/ ./
ENV HOST=0.0.0.0     PORT=8080     APP_ENV=production
EXPOSE 8080
CMD ["./bin/ktor-kotlin-microservice"]
