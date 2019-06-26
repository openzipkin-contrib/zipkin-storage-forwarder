.PHONY: all
all: build

OPEN := 'xdg-open'
MVN := 'mvn'
VERSION := '0.1.0-SNAPSHOT'

.PHONY: license-format
license-format:
	$(MVN) com.mycila:license-maven-plugin:3.0:format

.PHONY: build
build: license-format
	$(MVN) clean package

.PHONY: docker-build
docker-build:
	docker-compose build

.PHONY: download-zipkin
download-zipkin:
	curl -sSL https://zipkin.io/quickstart.sh | bash -s

.PHONY: zipkin-test-http
zipkin-test-http:
	curl -s https://raw.githubusercontent.com/apache/incubator-zipkin/master/zipkin-lens/testdata/netflix.json | \
	curl -X POST -s localhost:9412/api/v2/spans -H'Content-Type: application/json' -d @- ; \
	${OPEN} 'http://localhost:9411/zipkin/?lookback=custom&startTs=1'

.PHONY: zipkin-test-kafka
zipkin-test-kafka:
	curl -s https://raw.githubusercontent.com/openzipkin/zipkin/master/zipkin-lens/testdata/messaging-kafka.json | \
	curl -X POST -s localhost:9413/api/v2/spans -H'Content-Type: application/json' -d @- ; \
	${OPEN} 'http://localhost:9411/zipkin/?lookback=custom&startTs=1'
