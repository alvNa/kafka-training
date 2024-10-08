package com.atradius.services;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.stereotype.Service;

@Service
public class SourceProcessorService {


    public static final String INPUT_TOPIC = "selling-input-topic";

    public static final String OTHER_TOPIC = "output-topic";

    public void processSources(){
        // Get the source stream.
        final StreamsBuilder builder = new StreamsBuilder();

        //Implement streams logic.
        final KStream<String, String> source = builder.stream(INPUT_TOPIC);

        source.to(OTHER_TOPIC);
    }
}
