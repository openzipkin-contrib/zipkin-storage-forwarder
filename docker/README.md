## zipkin-storage-forwarder Docker images

To build a zipkin-storage-forwarder-http and zipkin-storage-forwarder-kafka Docker images, in the top level of the repository, run something
like

```bash
docker build -t openzipkincontrib/zipkin-storage-forwarder-http:test -f docker/http/Dockerfile .
docker build -t openzipkincontrib/zipkin-storage-forwarder-kafka:test -f docker/kafka/Dockerfile .
```
