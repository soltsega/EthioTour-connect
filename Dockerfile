# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/ethiotour-connect-1.0-SNAPSHOT.jar app.jar

# Note: Swing applications require an X11 server to run.
# To run this container on Linux with X11 forwarding:
# docker run -it --rm -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix ethiotour-connect

ENTRYPOINT ["java", "-jar", "app.jar"]
