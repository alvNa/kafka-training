package com.atradius.services;

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.tool.JsonToBinaryFragmentTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 1 get schema from Kafka Registry via REST
// 2 compiling the schema - Schema class
// 3 json to avro conversion
// 4 validating the avro against the schema
// 5 serializing the avro file in memory
// 6 sending the event with this custom serializer
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaRegistryService {
    final SchemaRegistryClient schemaRegistryClient;

    @Value("${spring.kafka.topics}")
    private String topicName;

    @Value("classpath:data/message-1.json")
    private Resource msgResourceFile;

    private JsonToBinaryFragmentTool jsonToBinaryFragmentTool = new JsonToBinaryFragmentTool();

    public Schema getSchemaFromRegistry() throws RestClientException, IOException {
        var schemaMetadata = schemaRegistryClient.getLatestSchemaMetadata(topicName + "-value");
        var schemaString = schemaMetadata.getSchema();
        Schema schema = new Schema.Parser().parse(schemaString);
        return schema;
    }

    public void compileSchema(String schema) throws Exception {
        var jsonResponse = Files.readString(msgResourceFile.getFile().toPath());
        //InputStream stdin = Files.newInputStream(msgResourceFile.getFile().toPath(), StandardOpenOption.READ);
        //jsonToBinaryFragmentTool.run(stdin,null,null,null);
        //var path = "/Users/AlvaroNav/Documents/github-alvNa/spring-kafka-demo/src/main/avro/Message.avsc";
        //var path = "/Users/AlvaroNav/Documents/github-alvNa/spring-kafka-demo/src/main/resources/data/message-1.json";
        //jsonToBinary(JSON, AVRO, "--schema-file", path);
        jsonToBinary(jsonResponse, AVRO, schema);

    }

    private static final String AVRO = "ZLong string implies readable length encoding.";
    private static final String JSON = "\"Long string implies readable length encoding.\"\n";
    private static final String UTF8 = "utf-8";

    private void jsonToBinary(String json, String avro, String... options) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(new BufferedOutputStream(baos));

        List<String> args = new ArrayList<>(Arrays.asList(options));
        args.add("-");
        new JsonToBinaryFragmentTool().run(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)), // stdin
                p, // stdout
                null, // stderr
                args);
        //assertEquals(avro, baos.toString(UTF8));
        p.println();

//        @Test
//        void jsonToBinarySchemaFile() throws Exception {
//            jsonToBinary(JSON, AVRO, "--schema-file", schemaFile(DIR));
//        }
    }
}
