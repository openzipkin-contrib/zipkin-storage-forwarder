package zipkin2.autoconfigure.storage.reporter;

import java.io.Serializable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import zipkin2.storage.StorageComponent;
import zipkin2.storage.reporter.ZipkinReporterStorage;
import zipkin2.storage.reporter.kafka.ZipkinKafkaReporterStorage;

@ConfigurationProperties("zipkin.storage.reporter")
public class ZipkinKafkaReporterStorageProperties implements Serializable {
  private static final long serialVersionUID = 0L;

  private String senderType;

  private String urlSenderEndpoint;

  private String kafkaBootstrapServers;

  private String encoding;

  public StorageComponent.Builder toBuilder() {
    return ZipkinKafkaReporterStorage.newBuilder()
        .encoding(encoding);
  }
}
