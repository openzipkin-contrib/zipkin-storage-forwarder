# Zipkin Forwarder [EXPERIMENTAL]

[![Build Status](https://travis-ci.org/openzipkin-contrib/zipkin-storage-forwarder.svg?branch=master)](https://travis-ci.org/openzipkin-contrib/zipkin-storage-forwarder)

Zipkin storage implementation to forward spans to another Zipkin server (collector).


Before:
```
[ instrumented client ] --> ( transport ) --> [ zipkin server ]
```

Now:
```
[ instrumented client ] --> ( transport 1 ) --> [ zipkin forwarder ] --> ( transport 2 ) --> [ zipkin server ]
```

Where `transport 1` could be HTTP or gRPC, and `transport 2` could be Kafka or RabbitMQ.

## Supported forwarders

- [HTTP](./http/README.md)
- [Kafka](./kafka/README.md)
