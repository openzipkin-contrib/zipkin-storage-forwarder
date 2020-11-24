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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.InternetProtocol;
import org.testcontainers.containers.wait.strategy.Wait;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.reporter.kafka.KafkaSender;

import static java.util.Collections.singletonList;
import static org.testcontainers.utility.DockerImageName.parse;

public class ITKafkaForwarderStorage {
  static final int KAFKA_PORT = 19092;

  static final class KafkaContainer extends GenericContainer<KafkaContainer> {
    KafkaContainer() {
      super(parse("ghcr.io/openzipkin/zipkin-kafka:2.23.0"));
      // 19092 is for connections from the Docker host and needs to be used as a fixed port.
      // TODO: someone who knows Kafka well, make ^^ comment better!
      addFixedExposedPort(KAFKA_PORT, KAFKA_PORT, InternetProtocol.TCP);
      this.waitStrategy = Wait.forHealthcheck();
    }
  }

  @Rule public KafkaContainer kafka = new KafkaContainer();

  KafkaSender sender;
  ForwarderStorage storage;

  @Before public void open() {
    sender = KafkaSender.newBuilder()
      .bootstrapServers(kafka.getContainerIpAddress() + ":" + kafka.getMappedPort(KAFKA_PORT))
      .encoding(Encoding.PROTO3)
      .build();

    storage = ForwarderStorage.newBuilder(sender).build();
  }

  @After public void close() {
    storage.close();
    sender.close();
  }

  @Test public void shouldSendSpansToUrlEndpoint() throws Exception {
    Span span = Span.newBuilder().traceId("a").id("b").name("test").duration(100).build();

    storage.spanConsumer().accept(singletonList(span)).execute();
  }
}
