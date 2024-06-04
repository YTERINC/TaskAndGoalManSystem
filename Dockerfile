FROM maven AS builder
COPY ./ ./
RUN mvn clean package -DskipTests
FROM openjdk:17-oracle
COPY --from=builder /target/TaskAndGoalManSystem-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8087
ENTRYPOINT ["java","-jar","/app.jar"]