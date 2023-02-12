FROM openjdk:8-jdk-alpine
RUN pwd && ls
EXPOSE 8080
ADD target/s8-project.jar s8-project.jar
ENTRYPOINT ["java","-jar","s8-project.jar"]