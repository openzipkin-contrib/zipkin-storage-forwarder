# HTTP Forwarder

Collects data from a transport (e.g. Kafka, HTTP) and forwards to HTTP.

## Configuration

| Config | Description | Default value |
|--------|-------------|---------------|
| `zipkin.storage.forwarder.http.endpoint` | Zipkin Spans API endpoint URL | `http://localhost:9411/api/v2/spans` |
| `zipkin.storage.forwarder.http.compression-enabled` | HTTP compression enabled | false |
| `zipkin.storage.forwarder.http.read-timeout` | HTTP read timeout. Default 60 * 1000 milliseconds. 0 implies no timeout. | false |
| `zipkin.storage.forwarder.http.connect-timeout` | HTTP connection timeout. Default 10 * 1000 milliseconds. 0 implies no timeout. | false |
| `zipkin.storage.forwarder.http.encoding` | Zipkin encoding: JSON, PROTO3. If ultimately sending to Zipkin, version 2.8+ is required to process protobuf. | JSON |
| `zipkin.storage.forwarder.http.message-max-bytes` | Message maximum bytes size. | 5MB |
