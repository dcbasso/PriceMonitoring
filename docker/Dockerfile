FROM eclipse-temurin:21.0.2_13-jre

ENV spring.datasource.username=postgres
ENV spring.datasource.password=pricemonitoring

COPY ../build/libs/pricemonitoring.jar /app/pricemonitoring.jar

ENTRYPOINT ["java", "-jar", "-Dserver.port=8080", "/app/pricemonitoring.jar"]

