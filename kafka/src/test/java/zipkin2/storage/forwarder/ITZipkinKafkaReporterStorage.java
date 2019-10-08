/*
 * Copyright 2019 The OpenZipkin Authors
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

import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.storage.StorageComponent;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertNull;

public class ITZipkinKafkaReporterStorage {

    @Rule
    public KafkaContainer kafka =
            new KafkaContainer("5.2.1")
                    .waitingFor(new HostPortWaitStrategy());

    @Test
    public void shouldSendSpansToKafka() {
        StorageComponent storage =
                ZipkinKafkaForwarderStorage.newBuilder()
                        .bootstrapServers(kafka.getBootstrapServers())
                        .encoding(Encoding.PROTO3.name())
                        .build();

        Span span = Span.newBuilder()
                .traceId("a")
                .id("b")
                .name("test")
                .duration(100)
                .build();

        try {
            Void v = storage.spanConsumer().accept(Collections.singletonList(span)).execute();
            assertNull(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}