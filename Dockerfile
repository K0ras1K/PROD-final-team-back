FROM gradle:8.7.0-jdk as build
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY gradle.properties .
COPY settings.gradle.kts .
COPY src src
COPY openapi openapi

#COPY build/libs/libraries libraries

RUN gradle copyDependencies -x test --no-daemon

RUN gradle build -x test --no-daemon --stacktrace

FROM openjdk:17-oracle

WORKDIR /app

COPY --from=build /workspace/app/build/libs/SampleAPI-0.0.1.jar ./app.jar
COPY --from=build /workspace/app/build/libs/libraries ./libraries
COPY /.env ./.env
COPY public public
COPY private private

CMD ["java", "-cp", "libraries/*:app.jar", "ru.droptableusers.sampleapi.ApplicationKt"]