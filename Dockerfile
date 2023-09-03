#
# Build stage
#
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/coronavirustracker-0.0.1-SNAPSHOT.jar coronavirustracker.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","coronavirustracker.jar"]