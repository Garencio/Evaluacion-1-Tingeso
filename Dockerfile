FROM openjdk:17
ARG JAR_FILE=target/TingesoProyecto1.jar
COPY ${JAR_FILE} TingesoProyecto1.jar
ENTRYPOINT ["java","-jar","/TingesoProyecto1.jar"]

