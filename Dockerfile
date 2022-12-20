FROM openjdk:8
EXPOSE 4000
ADD target/core_banking_app.jar core_banking_app.jar
ENTRYPOINT ["java","-jar","/core_banking_app.jar"]

