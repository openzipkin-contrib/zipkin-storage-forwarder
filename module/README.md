# module-storage-forwarder

## Overview

This is a module that can be added to a [Zipkin Server](https://github.com/openzipkin/zipkin/tree/master/zipkin-server)
deployment to send Spans to a different Zipkin transport.

## Quick start

JRE 8 is required to run Zipkin server.

Fetch the latest released
[executable jar for Zipkin server](https://search.maven.org/remote_content?g=io.zipkin&a=zipkin-server&v=LATEST&c=exec)
and
[module jar for forwarder storage](https://search.maven.org/remote_content?g=io.zipkin.contrib.zipkin-storage-forwarder&a=zipkin-module-storage-forwarder&v=LATEST&c=module).
Run Zipkin server with the Forwarder Storage enabled.

For example:

```bash
$ curl -sSL https://zipkin.io/quickstart.sh | bash -s
$ curl -sSL https://zipkin.io/quickstart.sh | bash -s io.zipkin.contrib.zipkin-storage-forwarder:zipkin-module-storage-forwarder:LATEST:module forwarder.jar
$ STORAGE_TYPE=http-forwarder \
    java \
    -Dloader.path='forwarder.jar,forwarder.jar!/lib' \
    -Dspring.profiles.active=forwarder \
    -cp zipkin.jar \
    org.springframework.boot.loader.launch.PropertiesLauncher
```
