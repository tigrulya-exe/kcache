FROM openjdk:11-jre-slim
ARG JAR_FILE=build/libs/*.jar
ARG PROPERTIES_PATH
COPY ${JAR_FILE} app.jar
ENV PROPERTIES_PATH ${PROPERTIES_PATH}
ENTRYPOINT java -Dspring.config.location=file:${PROPERTIES_PATH} -jar /app.jar