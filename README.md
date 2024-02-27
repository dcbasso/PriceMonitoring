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


