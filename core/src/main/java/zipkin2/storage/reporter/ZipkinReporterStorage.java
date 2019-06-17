package zipkin2.storage.reporter;

import java.util.List;
import zipkin2.Call;
import zipkin2.DependencyLink;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.codec.SpanBytesEncoder;
import zipkin2.reporter.Sender;
import zipkin2.storage.QueryRequest;
import zipkin2.storage.SpanConsumer;
import zipkin2.storage.SpanStore;
import zipkin2.storage.StorageComponent;

/**
 * Storage component to collect and report spans downstream.
 */
public abstract class ZipkinReporterStorage<S extends Sender> extends StorageComponent {

  private static final SpanStore NOOP_SPAN_STORE = new SpanStore() {
    @Override public Call<List<List<Span>>> getTraces(QueryRequest queryRequest) {
      return Call.emptyList();
    }

    @Override public Call<List<Span>> getTrace(String s) {
      return Call.emptyList();
    }

    @Override public Call<List<String>> getServiceNames() {
      return Call.emptyList();
    }

    @Override public Call<List<String>> getSpanNames(String s) {
      return Call.emptyList();
    }

    @Override public Call<List<DependencyLink>> getDependencies(long l, long l1) {
      return Call.emptyList();
    }
  };

  final S sender;
  final SpanBytesEncoder encoder;

  protected ZipkinReporterStorage(Builder<S> builder) {
    this.sender = builder.sender;
    this.encoder = builder.encoder;
  }

  @Override public SpanStore spanStore() {
    return NOOP_SPAN_STORE;
  }

  @Override public SpanConsumer spanConsumer() {
    return new ZipkinReporterSpanConsumer<>(this);
  }

  public static abstract class Builder<S extends Sender> extends StorageComponent.Builder {
    SpanBytesEncoder encoder;
    S sender;
    Encoding encoding;
    int messageMaxBytes;

    protected Builder() {
    }

    @Override public StorageComponent.Builder strictTraceId(boolean strictTraceId) {
      if (!strictTraceId) throw new IllegalArgumentException("non-strict trace ID not supported");
      return this;
    }

    @Override public StorageComponent.Builder searchEnabled(boolean searchEnabled) {
      if (searchEnabled) throw new IllegalArgumentException("search not supported");
      return this;
    }

    public Builder encoding(String encoding) {
      this.encoding = Encoding.valueOf(encoding);
      return this;
    }
  }
}
