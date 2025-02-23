package com.turculet.kstreams.config;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DirtiesContext()
@EmbeddedKafka(
        partitions = 1,
        topics = {"input1", "output1"})
@ActiveProfiles("test")
@ContextConfiguration
@TestMethodOrder(MethodOrderer.MethodName.class)
class KafkaStreamIT {

    private static final String INPUT_TOPIC = "input1";
    private static final String OUTPUT_TOPIC = "output1";
    private static final String GROUP_ID = "test";

    protected Producer<String, String> producer;
    protected Consumer<String, String> consumer;
    @Autowired
    protected EmbeddedKafkaBroker embeddedKafka;

    @BeforeEach
    void setUp() {
        Map<String, Object> okConsumerProps =
                KafkaTestUtils.consumerProps(
                        embeddedKafka.getBrokersAsString(), GROUP_ID, "false");
        okConsumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        okConsumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        consumer = new KafkaConsumer<>(okConsumerProps);
        consumer.subscribe(Arrays.asList(OUTPUT_TOPIC));

        Map<String, Object> producerProps =
                KafkaTestUtils.producerProps(embeddedKafka.getBrokersAsString());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer = new KafkaProducer<>(producerProps);

    }

    @AfterEach
    void tearDown() {
        consumer.close();
        producer.close();
    }

    @Test
    void testOkProcessing() throws Exception {
        // Send a dummy message to input topic
        String message = "hello kafka";
        ProducerRecord<String, String> record = new ProducerRecord<>(INPUT_TOPIC, message);
        RecordMetadata metadata = producer.send(record).get();
        assertThat(metadata).isNotNull();

        // Poll and validate the output message
        ConsumerRecords<String, String> consumerRecords = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(2));
        assertThat(consumerRecords.count()).isGreaterThan(0);

        Iterable<ConsumerRecord<String, String>> topicRecords = consumerRecords.records(OUTPUT_TOPIC);

        topicRecords.forEach(record1 -> {
            System.out.println("Value: " + record1.value());
        });

        System.out.println("Records size: " + consumerRecords.count());
    }

}