FROM openjdk:21-jdk

WORKDIR /app

ARG JAR_FILE="./build/libs/raceconditionattackdemo-*.jar"
ARG DOCKER_COMPOSE_PATH="./compose/app"
COPY $JAR_FILE ./application.jar
COPY $DOCKER_COMPOSE_PATH/ ./

CMD ["java", "-jar", "/app/application.jar"]
