package zipkin2.storage.reporter.urlconnection;

import zipkin2.reporter.urlconnection.URLConnectionSender;
import zipkin2.storage.StorageComponent;
import zipkin2.storage.reporter.ZipkinReporterStorage;

public class ZipkinURLConnectionReporterStorage extends ZipkinReporterStorage<URLConnectionSender> {

  ZipkinURLConnectionReporterStorage(Builder builder) {
    super(builder);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder extends ZipkinReporterStorage.Builder<URLConnectionSender> {
    @Override public StorageComponent build() {
      return null;
    }
  }
}
