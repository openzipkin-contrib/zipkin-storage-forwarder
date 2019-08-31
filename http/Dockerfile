FROM alpine

ENV USER jeqo
ENV VERSION master-SNAPSHOT

WORKDIR /zipkin

RUN apk add curl unzip && \
  curl -SL https://jitpack.io/com/github/${USER}/zipkin-forwarder/zipkin-forwarder-http-autoconfigure/${VERSION}/zipkin-forwarder-http-autoconfigure-${VERSION}-module.jar > forwarder.jar && \
  echo > .forwarder_profile && \
  unzip forwarder.jar -d forwarder && \
  rm forwarder.jar

FROM openzipkin/zipkin:2.16.2

COPY --from=0  --chown=zipkin /zipkin/ /zipkin/

ENV MODULE_OPTS="-Dloader.path=forwarder -Dspring.profiles.active=http-forwarder"
ENV STORAGE_TYPE=http-forwarder
