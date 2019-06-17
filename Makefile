.PHONY: all
all:

OPEN := 'xdg-open'
MVN := './mvnw'
VERSION := '0.1.0-SNAPSHOT'

.PHONY: license-format
license-format:
	$(MVN) com.mycila:license-maven-plugin:3.0:format
