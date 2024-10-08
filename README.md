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


## Kafka Connect

curl -X POST \
-H "Content-Type: application/vnd.kafka.json.v2+json" \
-H "Accept: application/vnd.kafka.v2+json" \
--data '{ \
    "name":"ftp_source_connector", \
    "config": { \
        "connector.class": "io.confluent.connect.ftps.FtpsSourceConnector", \
        "kafka.topic": "topic_connect", \
        "key.converter": "org.apache.kafka.connect.storage.StringConverter", \
        "value.converter": "org.apache.kafka.connect.storage.StringConverter", \
        "tasks.max": 3 \
    } \
} \
"http://localhost:8082/topics/purchases"

PHARMACY-SALES-TOPIC
name=sftp-sink-connector
topics=sftptopic
tasks.max=1
connector.class=io.confluent.connect.sftp.SftpSinkConnector

name=CsvSchemaSftp
kafka.topic=sftp-testing-topic
tasks.max=1
connector.class=io.confluent.connect.sftp.SftpCsvSourceConnector
cleanup.policy=NONE
behavior.on.error=IGNORE
input.path=/path/to/data
error.path=/path/to/error
finished.path=/path/to/finished
input.file.pattern=csv-sftp-source.csv
sftp.username=username
sftp.password=password
sftp.host=localhost
sftp.port=22
csv.first.row.as.header=false

curl -X POST http://localhost:8083/connectors -H "Content-Type: application/json" \
-d '{
    "name": "ftp_source_connector",
    "config": {
        "connector.class": "io.confluent.connect.sftp.SftpCsvSourceConnector",
        "cleanup.policy": "NONE",
        "behavior.on.error": "IGNORE",
        "kafka.topic": "pharmacy-sales-topic",
        "input.path": "/data",
        "error.path": "/error",
        "finished.path": "/data",
        "input.file.pattern": "people-100.csv",
        "tasks.max": 1,
        "sftp.host": "localhost",
        "sftp.username": "username",
        "sftp.password": "mypass",
        "sftp.port": 22,
        "csv.first.row.as.header": false,
        "schema.generation.enabled": true
    }
}'

EXAMPLE:

curl -X POST http://localhost:8083/connectors -H "Content-Type: application/json" \
-d '{ 
    "name": "jdbc_source_postgres", 
    "config": { 
        "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector", 
        "connection.url": "jdbc:postgresql://10.0.1.102:5432/inventory", 
        "connection.user": "kafka", 
        "connection.password": "Kafka!", 
        "topic.prefix": "postgres-", 
        "mode":"timestamp", 
        "timestamp.column.name": "update_ts" 
    } 
}' 