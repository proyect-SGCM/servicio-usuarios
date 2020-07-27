FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8082
ADD target/*.jar servicio-usuarios.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/servicio-usuarios.jar"]