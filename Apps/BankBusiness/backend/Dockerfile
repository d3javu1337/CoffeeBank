FROM sbtscala/scala-sbt:eclipse-temurin-alpine-17.0.15_6_1.12.11_2.12.21 AS builder
WORKDIR /app
RUN apk add --no-cache protobuf-dev gcompat
RUN rm -rf /root/.cache/protocbridge

COPY build.sbt ./
COPY project/ project/
RUN sbt update
COPY . .
RUN find /app/target -name "*.jar" -type f
RUN sbt assembly

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/scala-*/*.jar app.jar
COPY --from=builder /app/target/scala-*/*.jar app.jar
EXPOSE 1337
ENTRYPOINT ["java", "-Dconfig.resource=docker.conf", "-jar", "app.jar"]