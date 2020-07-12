# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="thi.perrut@gmail.com"

# The application's jar file
ARG JAR_FILE=target/boleto-api-1.0.0.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]