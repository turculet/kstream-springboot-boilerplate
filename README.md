# Spring Boot Kafka Streams Boilerplate

A Spring Boot application template that demonstrates the implementation of Kafka Streams processing pipeline. This boilerplate provides a foundation for building stream processing applications that read from an input topic and write to an output topic.

## Technologies

- Java 23
- Spring Boot
- Apache Kafka Streams
- Gradle
- Embedded Kafka (for testing)

## Prerequisites

Before you begin, ensure you have the following installed:
- JDK 23
- Gradle 8.x
- Docker (for running Kafka locally)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/turculet/kstream-springboot-boilerplate
cd kstream-springboot-boilerplate
```

### Build the Project

```bash
./gradlew clean build
```

### Run the Application

```bash
./gradlew bootRun
```

## Configuration

### Application Properties

Configure your Kafka connection and stream properties in `application.yml`:

### Kafka Topics

The application uses the following topics:
- Input Topic: `input1`
- Output Topic: `output1`

## Testing

The project includes integration tests using embedded Kafka. Run the tests with:

```bash
./gradlew test
```

### Integration Tests

The integration tests demonstrate:
- Stream processing pipeline validation
- Data transformation verification

## Usage

1. Ensure Docker is running locally
2. Start the application
3. Publish messages to the input topic
4. Observe processed messages in the output topic

Example using kafka-console-producer/consumer:

```bash
# Produce message
kafka-console-producer --bootstrap-server localhost:9092 --topic input-topic

# Consume processed message
kafka-console-consumer --bootstrap-server localhost:9092 --topic output-topic --from-beginning
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.