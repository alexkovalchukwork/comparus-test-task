FROM maven:3.6.3-openjdk-17 AS build
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

FROM openjdk:17-jdk
COPY --from=build /usr/src/app/target/service-0.0.1-SNAPSHOT.jar /usr/app/service-0.0.1-SNAPSHOT.jar
EXPOSE 8080  
CMD ["java","-jar","/usr/app/service-0.0.1-SNAPSHOT.jar"]