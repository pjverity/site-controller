FROM openjdk:13.0.1-slim
COPY target/site-controller-*.jar site-controller.jar
EXPOSE 8080
CMD java ${JAVA_OPTS} -jar site-controller.jar