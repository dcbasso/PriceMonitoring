FROM eclipse-temurin:21.0.2_13-jre

ENV spring.datasource.username=postgres
ENV spring.datasource.password=pricemonitoring
ENV exchangeratesapi.api-key=
ENV TZ=America/Sao_Paulo
ENV capture.cron.scheduled="0 0 8-17 * * *"
ENV SPRING_PROFILES_ACTIVE=docker
ENV spring.mail.host=smtp.dreamhost.com
ENV spring.mail.port=465
ENV spring.mail.username=${YOUR_EMAIL}
ENV spring.mail.password=${YOUR_PASSWORD}
ENV spring.mail.properties.debug=true
ENV spring.mail.properties.transport.protocol=smtp
ENV spring.mail.properties.mail.smtp.username=${YOUR_EMAIL}
ENV spring.mail.properties.mail.smtp.auth=true
ENV spring.mail.properties.mail.smtp.starttls.enable=true
ENV spring.mail.properties.mail.smtp.starttls.required=true
ENV spring.mail.properties.mail.smtp.ssl.enable=true
ENV spring.mail.properties.mail.smtp.connectiontimeout=5000
ENV spring.mail.properties.mail.smtp.timeout=5000
ENV spring.mail.properties.mail.smtp.writetimeout=5000
ENV spring.mail.properties.mail.smtp.from=${YOUR_EMAIL}
ENV spring.mail.test-connection=true
ENV notification.email.enabled=true
ENV notification.email.destiny=${YOUR_EMAIL_DESTINY}
ENV notification.email.identification=${YOUR_EMAIL_IDENTIFICATION}
ENV jobs.enabled=true
ENV jobs.stores.list="visaovip, topdek"
ENV jobs.extras.list="currencyquote"

COPY ../build/libs/pricemonitoring.jar /app/pricemonitoring.jar

ENTRYPOINT ["java", "-jar", "-Dserver.port=8080", "/app/pricemonitoring.jar"]

