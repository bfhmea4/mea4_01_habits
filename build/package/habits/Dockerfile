## Backend build
FROM maven:3.8-eclipse-temurin-17 as build

WORKDIR /app
COPY ./pom.xml ./pom.xml
COPY ./src ./src

RUN mvn dependency:go-offline -B
RUN mvn package

# Production build
FROM openjdk:17-alpine

WORKDIR /usr/src/habits

#RUN addgroup -g 1001 -S habits && \
#    adduser -S habits -u 1001

#COPY --from=build --chown=habits:habits /src/target/habits-*.jar /usr/src/habits/habits.jar
COPY --from=build /app/target/habits-*.jar /usr/src/habits/habits.jar

EXPOSE 8080

CMD ["java", "-jar", "/usr/src/habits/habits.jar"]
