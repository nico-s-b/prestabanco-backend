FROM openjdk:21
ARG JAR_FILE=target/prestabanco1.jar
COPY ${JAR_FILE} prestabanco1.jar
ENTRYPOINT ["java", "-jar", "/prestabanco1.jar"]