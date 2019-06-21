# Kafka Forwarder

Collects data from a transport (e.g. Kafka, HTTP) and forwards to a Kafka topic.

## Configuration

| Config | Description | Default value |
|--------|-------------|---------------|
| `zipkin.storage.forwarder.kafka.bootstrap-servers` | List of Kafka brokers to bootstrap connection | localhost:9092 |
| `zipkin.storage.forwarder.kafka.topic` | Topic name to send spans to | zipkin |
| `zipkin.storage.forwarder.kafka.overrides` | Additional Kafka producer configuration | empty |
| `zipkin.storage.forwarder.kafka.encoding` | Zipkin encoding: JSON, PROTO3 | PROTO3 |
| `zipkin.storage.forwarder.kafka.message-max-bytes` | Message maximum bytes size. | 1_000_000 |
