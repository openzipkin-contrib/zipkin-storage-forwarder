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
import org.opentest4j.TestAbortedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.InternetProtocol;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.reporter.kafka.KafkaSender;

import static java.util.Collections.singletonList;
import static org.testcontainers.utility.DockerImageName.parse;

@Tag("docker")
@Testcontainers(disabledWithoutDocker = true)
public class ITKafkaForwarderStorage {
  static final Logger LOGGER = LoggerFactory.getLogger(ITKafkaForwarderStorage.class);

  static final int KAFKA_PORT = 19092;

  // mostly waiting for https://github.com/testcontainers/testcontainers-java/issues/3537
  static final class KafkaContainer extends GenericContainer<KafkaContainer> {
    KafkaContainer() {
      super(parse("ghcr.io/openzipkin/zipkin-kafka:3.0.2"));
      waitStrategy = Wait.forHealthcheck();
      // 19092 is for connections from the Docker host and needs to be used as a fixed port.
      // TODO: someone who knows Kafka well, make ^^ comment better!
      addFixedExposedPort(KAFKA_PORT, KAFKA_PORT, InternetProtocol.TCP);
      withLogConsumer(new Slf4jLogConsumer(LOGGER));
    }
  }

  @Container public KafkaContainer kafka = new KafkaContainer();

  KafkaSender sender;
  ForwarderStorage storage;

  @BeforeEach public void open() {
    sender = KafkaSender.newBuilder()
      .bootstrapServers(kafka.getHost() + ":" + kafka.getMappedPort(KAFKA_PORT))
      .encoding(zipkin2.reporter.Encoding.PROTO3)
      .build();

    storage = ForwarderStorage.newBuilder(sender).build();
  }

  @AfterEach public void close() {
    storage.close();
    sender.close();
  }

  @Test public void shouldSendSpansToUrlEndpoint() throws Exception {
    Span span = Span.newBuilder().traceId("a").id("b").name("test").duration(100).build();

    storage.spanConsumer().accept(singletonList(span)).execute();
  }
}
