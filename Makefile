.PHONY: all
all: build

OPEN := 'xdg-open'
MVN := './mvnw'
# Maven tasks
## Add license headers
.PHONY: license-format
license-format:
	${MVN} com.mycila:license-maven-plugin:3.0:format
## Build artifacts
.PHONY: build
build: license-format
	${MVN} clean package
# Testing task
## Test HTTP forwarder
.PHONY: zipkin-test-http
zipkin-test-http:
	curl -s https://raw.githubusercontent.com/apache/incubator-zipkin/master/zipkin-lens/testdata/netflix.json | \
	curl -X POST -s localhost:19411/api/v2/spans -H'Content-Type: application/json' -d @- ; \
	${OPEN} 'http://localhost:9411/zipkin/?lookback=custom&startTs=1'
## Test Kafka forwarder
.PHONY: zipkin-test-kafka
zipkin-test-kafka:
	curl -s https://raw.githubusercontent.com/openzipkin/zipkin/master/zipkin-lens/testdata/messaging-kafka.json | \
	curl -X POST -s localhost:29411/api/v2/spans -H'Content-Type: application/json' -d @- ; \
	${OPEN} 'http://localhost:9411/zipkin/?lookback=custom&startTs=1'
