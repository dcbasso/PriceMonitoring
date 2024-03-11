FROM eclipse-temurin:21.0.2_13-jre

ENV spring.datasource.username=postgres
ENV spring.datasource.password=pricemonitoring
ENV exchangeratesapi.api-key=
ENV TZ=America/Sao_Paulo
ENV capture.cron.scheduled="0 0 8-17 * * *"
ENV SPRING_PROFILES_ACTIVE=docker

COPY ../build/libs/pricemonitoring.jar /app/pricemonitoring.jar

ENTRYPOINT ["java", "-jar", "-Dserver.port=8080", "/app/pricemonitoring.jar"]

