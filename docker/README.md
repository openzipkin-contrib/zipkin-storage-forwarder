# zipkin-storage-forwarder Docker image

To build a zipkin-storage-forwarder Docker image, in the top level of the repository, run something
like

## Building

To build a zipkin-storage-forwarder Docker image from source, in the top level of the repository, run:


```bash
$ build-bin/docker/docker_build openzipkin-contrib/zipkin-storage-forwarder:test
```

To build from a published version, run this instead:

```bash
$ build-bin/docker/docker_build openzipkin-contrib/zipkin-storage-forwarder:test 0.18.1
```
