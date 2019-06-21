# Zipkin Forwarder

> alpha stage

[![Build Status](https://travis-ci.org/jeqo/zipkin-forwarder.svg?branch=master)](https://travis-ci.org/jeqo/zipkin-forwarder)

Zipkin storage implementation to forward Spans to another server (collector).


Before:
```
[ instrumented client ] --> ( transport ) --> [ zipkin server ]
```

Now:
```
[ instrumented client ] --> ( transport 1 ) --> [ zipkin forwarder ] --> ( transport 2 ) --> [ zipkin server ]
```

## Supported forwarders

- [HTTP](./http/README.md)
- [Kafka](./kafka/README.md)
