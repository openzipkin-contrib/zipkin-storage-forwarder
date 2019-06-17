package zipkin2.storage.reporter.kafka;

import zipkin2.reporter.kafka11.KafkaSender;
import zipkin2.storage.StorageComponent;
import zipkin2.storage.reporter.ZipkinReporterStorage;

public class ZipkinKafkaReporterStorage extends ZipkinReporterStorage<KafkaSender> {
  ZipkinKafkaReporterStorage(Builder builder) {
    super(builder);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder extends ZipkinReporterStorage.Builder<KafkaSender> {

    @Override public StorageComponent build() {
      return null;
    }
  }
}
