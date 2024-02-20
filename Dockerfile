FROM eclipse-temurin:21-jre-alpine

COPY target/*.jar app.jar
ENTRYPOINT ["java","--enable-preview","-jar","/app.jar"]

# for HTTP
EXPOSE 8080
