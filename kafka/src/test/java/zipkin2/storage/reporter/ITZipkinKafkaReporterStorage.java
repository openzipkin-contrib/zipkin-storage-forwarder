package zipkin2.storage.reporter;

import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
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
                ZipkinKafkaReporterStorage.newBuilder()
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