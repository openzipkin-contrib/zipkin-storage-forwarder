package zipkin2.storage.reporter;

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

public class ITZipkinURLConnectionReporterStorage {

    @Rule
    public GenericContainer zipkin =
            new GenericContainer("openzipkin/zipkin")
                    .withExposedPorts(9411)
            .waitingFor(new HttpWaitStrategy());

    @Test
    public void shouldSendSpansToUrlEndpoint() {
        StorageComponent storage =
                ZipkinURLConnectionReporterStorage.newBuilder()
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