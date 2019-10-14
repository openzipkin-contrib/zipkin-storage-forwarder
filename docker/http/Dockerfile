#
# Copyright 2019 The OpenZipkin Authors
#
# Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
# in compliance with the License. You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software distributed under the License
# is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
# or implied. See the License for the specific language governing permissions and limitations under
# the License.
#

FROM openzipkin/zipkin-builder

WORKDIR /code
COPY . /code
RUN mvn -B --no-transfer-progress package -DskipTests=true -pl autoconfigure/http -am

RUN \
  cd /code && mkdir -p /zipkin-forwarder && cp autoconfigure/http/target/zipkin-autoconfigure-storage-forwarder-http-*-module.jar /zipkin-forwarder && cd /zipkin-forwarder && jar xf *.jar && rm *.jar

FROM openzipkin/zipkin:2.17.2
MAINTAINER Zipkin "https://zipkin.io/"

COPY --from=0 /zipkin-forwarder/ /zipkin/lib/

RUN echo > .forwarder_profile

ENV MODULE_OPTS="-Dloader.path=lib -Dspring.profiles.active=http-forwarder"
ENV STORAGE_TYPE=http-forwarder
