FROM openjdk:8-jdk-alpine
VOLUME /home/circleci/project
EXPOSE 8080
ADD target/s8-project.jar s8-project.jar
ENTRYPOINT ["java","-jar","s8-project.jar"]