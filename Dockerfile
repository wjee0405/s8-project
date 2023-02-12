FROM openjdk:8-jdk-alpine
RUN ls home
EXPOSE 8080
ADD /home/circleci/project/target/s8-project.jar s8-project.jar
ENTRYPOINT ["java","-jar","s8-project.jar"]