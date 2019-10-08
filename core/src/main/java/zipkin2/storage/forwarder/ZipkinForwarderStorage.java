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

import java.io.IOException;
import java.util.List;
import zipkin2.Call;
import zipkin2.CheckResult;
import zipkin2.DependencyLink;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.storage.QueryRequest;
import zipkin2.storage.SpanConsumer;
import zipkin2.storage.SpanStore;
import zipkin2.storage.StorageComponent;

/**
 * Storage component to collect and report spans downstream.
 */
public abstract class ZipkinForwarderStorage<S extends Sender> extends StorageComponent {

  static final SpanStore NOOP_SPAN_STORE = new SpanStore() {
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

  //final AsyncReporter.Builder reporterBuilder;
  final Sender sender;

  volatile AsyncReporter<Span> reporter;

  ZipkinForwarderStorage(Builder<S> builder) {
    this.sender = builder.sender();
  }

  AsyncReporter<Span> get() {
    if (reporter == null) {
      synchronized (this) {
        if (reporter == null) {
          reporter = AsyncReporter.builder(sender).build();
        }
      }
    }
    return reporter;
  }

  @Override public SpanStore spanStore() {
    return NOOP_SPAN_STORE;
  }

  @Override public SpanConsumer spanConsumer() {
    return new ZipkinForwarderSpanConsumer<>(this);
  }

  @Override public CheckResult check() {
    return get().check();
  }

  @Override public void close() throws IOException {
    if (reporter != null) {
      get().flush();
      get().close();
    }
  }

  public static abstract class Builder<S extends Sender> extends StorageComponent.Builder {
    Encoding encoding = Encoding.JSON;
    Integer messageMaxBytes;

    protected Builder() {
    }

    @Override
    public Builder strictTraceId(boolean strictTraceId) {
      if (!strictTraceId) throw new IllegalArgumentException("non-strict trace ID not supported");
      return this;
    }

    @Override
    public Builder searchEnabled(boolean searchEnabled) {
      if (searchEnabled) throw new IllegalArgumentException("search not supported");
      return this;
    }

    public Builder encoding(String encoding) {
      if (encoding == null) throw new NullPointerException("encoding == null");
      this.encoding = Encoding.valueOf(encoding);
      return this;
    }

    public Builder messageMaxBytes(Integer messageMaxBytes) {
      this.messageMaxBytes = messageMaxBytes;
      return this;
    }

    abstract S sender();
  }
}
