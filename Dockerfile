FROM openjdk:17

ADD target/invoice-service.jar invoice-service.jar

COPY target/invoice-service.jar invoice-service.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","invoice-service.jar"]