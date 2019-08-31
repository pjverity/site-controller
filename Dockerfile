FROM openjdk:12.0.2-jdk
COPY target/site-controller-*.jar site-controller.jar
EXPOSE 8080
CMD java ${JAVA_OPTS} -jar site-controller.jar