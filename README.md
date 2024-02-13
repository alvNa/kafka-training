# spring-kafka-demo

Spring Kafka example of how to configure a Kafka consumer and producer using Spring Boot.

## Setup docker environment

```bash
docker-compose -f src/test/docker/docker-compose.yml up -d
```
## Run the application

```bash
mvn spring-boot:run
```

In the resources folder, you can find the `application.yml` file with the Kafka configuration, and a
`application-azure.yml`. This azure profile illustrates how you should configure using the Azure Event Hub.