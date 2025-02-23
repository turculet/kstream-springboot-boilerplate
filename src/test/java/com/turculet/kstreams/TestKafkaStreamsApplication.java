package com.turculet.kstreams;

import org.springframework.boot.SpringApplication;

public class TestKafkaStreamsApplication {

	public static void main(String[] args) {
		SpringApplication.from(KafkaStreamsApplication::main).run(args);
	}

}
