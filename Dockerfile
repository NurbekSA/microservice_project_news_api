FROM openjdk:19

WORKDIR /app

COPY target/scala-2.13/News_api-assembly-0.0.1.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]