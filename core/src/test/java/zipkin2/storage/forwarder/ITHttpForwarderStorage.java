/*
 * Copyright 2019-2024 The OpenZipkin Authors
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import zipkin2.Span;
import zipkin2.reporter.okhttp3.OkHttpSender;

import static java.util.Collections.singletonList;
import static org.testcontainers.utility.DockerImageName.parse;

@Tag("docker")
@Testcontainers(disabledWithoutDocker = true)
class ITHttpForwarderStorage {
  static final Logger LOGGER = LoggerFactory.getLogger(ITKafkaForwarderStorage.class);

  static final class ZipkinContainer extends GenericContainer<ZipkinContainer> {
    ZipkinContainer() {
      super(parse("ghcr.io/openzipkin/zipkin-slim:3.0.0"));
      waitStrategy = Wait.forHealthcheck();
      withExposedPorts(9411);
      withLogConsumer(new Slf4jLogConsumer(LOGGER));
    }
  }

  @Container ZipkinContainer zipkin = new ZipkinContainer();

  OkHttpSender sender;
  ForwarderStorage storage;

  @BeforeEach void open() {
    sender = OkHttpSender.newBuilder()
      .endpoint("http://" + zipkin.getHost() + ":" + zipkin.getMappedPort(9411) + "/api/v2/spans")
      .encoding(zipkin2.reporter.Encoding.JSON)
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
