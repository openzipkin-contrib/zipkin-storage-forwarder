# Zipkin Storage Forwarder **EXPERIMENTAL**

[![Gitter chat](http://img.shields.io/badge/gitter-join%20chat%20%E2%86%92-brightgreen.svg)](https://gitter.im/openzipkin/zipkin)
[![Build Status](https://github.com/openzipkin-contrib/zipkin-storage-forwarder/workflows/test/badge.svg)](https://github.com/openzipkin-contrib/zipkin-storage-forwarder/actions?query=workflow%3Atest)
[![Maven Central](https://img.shields.io/maven-central/v/io.zipkin.contrib.zipkin-storage-forwarder/zipkin-module-storage-forwarder.svg)](https://search.maven.org/search?q=g:io.zipkin.contrib.zipkin-storage-forwarder%20AND%20a:zipkin-module-storage-forwarder)


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

## Artifacts
All artifacts publish to the group ID "io.zipkin.contrib.zipkin-storage-forwarder". We use a common
release version for all components.

### Library Releases
Releases are at [Sonatype](https://oss.sonatype.org/content/repositories/releases) and [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.zipkin.contrib.zipkin-storage-forwarder%22)

### Library Snapshots
Snapshots are uploaded to [Sonatype](https://oss.sonatype.org/content/repositories/snapshots) after
commits to master.

### Docker Images
Released versions of zipkin-storage-forwarder are published to GitHub Container Registry as
`ghcr.io/openzipkin-contrib/zipkin-storage-forwarder`. See [docker](./docker) for details.
