FROM openjdk:17-oracle
ARG JAR_FILE=target/TaskAndGoalManSystem-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ./ ./
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]