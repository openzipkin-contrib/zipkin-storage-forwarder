/*
 * Copyright 2019-2020 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package zipkin2.storage.forwarder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.reporter.okhttp3.OkHttpSender;

import static java.util.Collections.singletonList;
import static org.testcontainers.utility.DockerImageName.parse;

@Testcontainers
class ITHttpForwarderStorage {
  static final Logger LOGGER = LoggerFactory.getLogger(ITKafkaForwarderStorage.class);

  // mostly waiting for https://github.com/testcontainers/testcontainers-java/issues/3537
  static final class ZipkinContainer extends GenericContainer<ZipkinContainer> {
    ZipkinContainer() {
      super(parse("ghcr.io/openzipkin/zipkin-slim:2.23.1"));
      if ("true".equals(System.getProperty("docker.skip"))) {
        throw new TestAbortedException("${docker.skip} == true");
      }
      waitStrategy = Wait.forHealthcheck();
      withLogConsumer(new Slf4jLogConsumer(LOGGER));
    }
  }

  @Container ZipkinContainer zipkin = new ZipkinContainer();

  OkHttpSender sender;
  ForwarderStorage storage;

  @BeforeEach void open() {
    sender = OkHttpSender.newBuilder()
      .endpoint("http://" + zipkin.getContainerIpAddress() + ":" + zipkin.getMappedPort(9411)
        + "/api/v2/spans")
      .encoding(Encoding.JSON)
      .build();

    storage = ForwarderStorage.newBuilder(sender).build();
  }

  @AfterEach void close() {
    storage.close();
    sender.close();
  }

  @Test void shouldSendSpansToUrlEndpoint() throws Exception {
    Span span = Span.newBuilder().traceId("a").id("b").name("test").duration(100).build();

    storage.spanConsumer().accept(singletonList(span)).execute();
  }
}
