/*
 * Copyright 2019 Jorge Esteban Quilcate Otoya
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
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.storage.StorageComponent;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertNull;

public class ITZipkinHttpForwarderStorage {

    @Rule
    public GenericContainer zipkin =
            new GenericContainer("openzipkin/zipkin")
                    .withExposedPorts(9411)
            .waitingFor(new HttpWaitStrategy());

    @Test
    public void shouldSendSpansToUrlEndpoint() {
        StorageComponent storage =
                ZipkinHttpForwarderStorage.newBuilder()
                        .endpoint("http://" + zipkin.getContainerIpAddress() + ":" + zipkin.getFirstMappedPort() + "/api/v2/spans")
                        .encoding(Encoding.JSON.name())
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