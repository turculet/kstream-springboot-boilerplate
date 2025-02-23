package com.turculet.kstreams.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

@Configuration
@EnableKafkaStreams
@Slf4j
public class KafkaStreamsConfig {

    @Value("${parser.stream.input.topic}")
    private String inputTopicName;

    @Value("${parser.stream.output.topic}")
    private String outputTopicName;

    @Bean
    public KStream<String, String> stream(StreamsBuilder builder, StreamsBuilderFactoryBean streamsBuilderFactoryBean, KafkaStreamsConfiguration kafkaStreamsConfiguration) {
        KStream<String, String> stream = builder.stream(inputTopicName, Consumed.with(Serdes.String(), Serdes.String()));

        stream.map(KeyValue::new).to(outputTopicName, Produced.with(Serdes.String(), Serdes.String()));

        streamsBuilderFactoryBean.setStreamsUncaughtExceptionHandler(exception -> {
            log.error(exception.getMessage(), exception);
            return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
        });

        return stream;
    }

}
