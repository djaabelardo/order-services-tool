FROM openjdk:8-jre-alpine

COPY /build/libs/order-services-tool.jar order-services-tool.jar

ENTRYPOINT ["java","-jar","order-services-tool.jar"]

