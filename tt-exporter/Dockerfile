FROM gradle:latest AS builder
COPY --chown=gradle:gradle . /home/gradle/app
WORKDIR /home/gradle/app
RUN gradle shadowJar --no-daemon

FROM adoptopenjdk
EXPOSE 7077
WORKDIR /app
COPY --from=builder /home/gradle/app/build/libs/ .
ENTRYPOINT java -jar *.jar
