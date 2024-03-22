## Project to Monitoring Prices on some Paraguai Stores

It's like a BI project, to capture and organize the information to know the historical prices of some stores in Paraguay (Ciudad del Este)

## Main stack

Kotlin
Liquibase
Docker
Postgresql (Docker)
Adminer (Docker)
SpringBoot

## Main objective

Use IA (ChatGPT 3.5 and Google Gemini) to help creating all necessary code and check how much this IA could help a DEV to make all the necessary code

# External/Vendors APIS

Currencies:
https://app.exchangerate-api.com/

Cripto Currencies:

# Docker

on root folder, create the image:
- When building on amd64:
docker build -t dcbasso/price-monitoring -f docker/Dockerfile .

- When building on Mac with M1/M2/M3 (arm64):
DOCKER_DEFAULT_PLATFORM=linux/amd64 docker build -t dcbasso/price-monitoring -f docker/Dockerfile .

- Uploading to docker hub:
docker push dcbasso/price-monitoring:latest


# Parameters

- To run the project, you need to set the following environment variables:

| Atributo                                      | Valor Padrão                                            |
|-----------------------------------------------|---------------------------------------------------------|
| **DATASOURCE/JPA**                            |                                                         |
|                                               |                                                         |
| spring.datasource.url                        | jdbc:postgresql://localhost:5432/pricemonitoring_test_31 |
| spring.datasource.username                   | postgres                                                |
| spring.datasource.password                   | pricemonitoring                                         |
| spring.jpa.show-sql                          | false                                                   |
| spring.jpa.hibernate.ddl-auto                | update                                                  |
|                                               |                                                         |
| **LIQUIBASE**                                 |                                                         |
|                                               |                                                         |
| spring.liquibase.change-log                  | classpath:db/liquibase-changelog.xml                    |
| spring.liquibase.enabled                     | true                                                    |
|                                               |                                                         |
| **EXCHANGERATE-API (https://app.exchangerate-api.com/)** |                                                         |
|                                               |                                                         |
| exchangeratesapi.api-key                     | ${YOUR_KEY}                                             |
|                                               |                                                         |
| **SCHEDULED**                                 |                                                         |
|                                               |                                                         |
| capture.cron.scheduled                       | 0 0 8-17 * * *                                          |
|                                               |                                                         |
| **EMAIL SERVICE**                             |                                                         |
|                                               |                                                         |
| spring.mail.properties.debug                 | false                                                   |
| spring.mail.host                             | smtp.dreamhost.com                                      |
| spring.mail.port                             | 465                                                     |
| spring.mail.username                         | ${YOUR_EMAIL}                                           |
| spring.mail.password                         | ${YOUR_PASSWORD}                                        |
| spring.mail.properties.transport.protocol   | smtp                                                    |
| spring.mail.properties.mail.smtp.username   | ${YOUR_EMAIL}                                           |
| spring.mail.properties.mail.smtp.auth       | true                                                    |
| spring.mail.properties.mail.smtp.starttls.enable | true                                                    |
| spring.mail.properties.mail.smtp.starttls.required | true                                                    |
| spring.mail.properties.mail.smtp.ssl.enable | true                                                    |
| spring.mail.properties.mail.smtp.connectiontimeout | 5000                                                    |
| spring.mail.properties.mail.smtp.timeout   | 5000                                                    |
| spring.mail.properties.mail.smtp.writetimeout | 5000                                                    |
| spring.mail.properties.mail.smtp.from      | ${YOUR_EMAIL}                                           |
| spring.mail.test-connection                  | true                                                    |
| notification.email.enabled                   | false                                                   |
| notification.email.destiny                   | ${YOUR_EMAIL_DESTINY}                                   |
| notification.email.identification            | ${TEXT_FOR_ENV_IDENTIFICATION}                          |
|                                               |                                                         |
| **JOBS**                                      |                                                         |
|                                               |                                                         |
| jobs.enabled                                 | true                                                    |
| jobs.stores.list                             | visaovip, topdek                                        |
| jobs.extras.list                             | currencyquote                                           |
| jobs.database-update.enabled                 | true                                                    |
