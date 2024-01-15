/*
 * Copyright 2019-2024 The OpenZipkin Authors
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

import java.util.Collections;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import zipkin2.Call;
import zipkin2.CheckResult;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.BytesMessageSender;
import zipkin2.reporter.ReporterMetrics;
import zipkin2.storage.AutocompleteTags;
import zipkin2.storage.ServiceAndSpanNames;
import zipkin2.storage.SpanConsumer;
import zipkin2.storage.SpanStore;
import zipkin2.storage.StorageComponent;
import zipkin2.storage.Traces;

/**
 * Write-only {@link zipkin2.storage.StorageComponent}, which forwards {@linkplain Span spans} to
 * {@link AsyncReporter#report(Object)}.
 */
public final class ForwarderStorage extends StorageComponent {
  public static Builder newBuilder(BytesMessageSender sender) {  // visible for testing
    return new Builder(sender);
  }

  public static final class Builder extends StorageComponent.Builder {
    final BytesMessageSender sender;
    final AsyncReporter.Builder delegate;

    Builder(BytesMessageSender sender) {
      this.sender = sender;
      delegate = AsyncReporter.builder(sender);
    }

    @Override public Builder strictTraceId(boolean searchEnabled) {
      return this; // ignore
    }

    @Override public Builder searchEnabled(boolean searchEnabled) {
      return this; // ignore
    }

    /** @see AsyncReporter.Builder#threadFactory(ThreadFactory) */
    public Builder threadFactory(ThreadFactory threadFactory) {
      delegate.threadFactory(threadFactory);
      return this;
    }

    /** @see AsyncReporter.Builder#metrics(ReporterMetrics) */
    public Builder metrics(ReporterMetrics metrics) {
      delegate.metrics(metrics);
      return this;
    }

    /** @see AsyncReporter.Builder#messageMaxBytes(int) */
    public Builder messageMaxBytes(int messageMaxBytes) {
      delegate.messageMaxBytes(messageMaxBytes);
      return this;
    }

    /** @see AsyncReporter.Builder#messageTimeout(long, TimeUnit) */
    public Builder messageTimeout(long timeout, TimeUnit unit) {
      delegate.messageTimeout(timeout, unit);
      return this;
    }

    /** @see AsyncReporter.Builder#closeTimeout(long, TimeUnit) */
    public Builder closeTimeout(long timeout, TimeUnit unit) {
      delegate.closeTimeout(timeout, unit);
      return this;
    }

    /** @see AsyncReporter.Builder#queuedMaxSpans(int) */
    public Builder queuedMaxSpans(int queuedMaxSpans) {
      delegate.queuedMaxSpans(queuedMaxSpans);
      return this;
    }

    /** @see AsyncReporter.Builder#queuedMaxBytes(int) */
    public Builder queuedMaxBytes(int queuedMaxBytes) {
      delegate.queuedMaxBytes(queuedMaxBytes);
      return this;
    }

    @Override public ForwarderStorage build() {
      return new ForwarderStorage(this);
    }
  }

  final BytesMessageSender sender;
  final AsyncReporter<Span> asyncReporter;

  ForwarderStorage(Builder builder) {
    this.sender = builder.sender;
    this.asyncReporter = builder.delegate.build();
  }

  @Override public SpanStore spanStore() {
    throw new UnsupportedOperationException("Read operations are not supported");
  }

  @Override public Traces traces() {
    throw new UnsupportedOperationException("Read operations are not supported");
  }

  @Override public AutocompleteTags autocompleteTags() {
    throw new UnsupportedOperationException("Read operations are not supported");
  }

  @Override public ServiceAndSpanNames serviceAndSpanNames() {
    throw new UnsupportedOperationException("Read operations are not supported");
  }

  @Override public SpanConsumer spanConsumer() {
    return new ForwarderSpanConsumer(asyncReporter);
  }

  @Override public CheckResult check() {
    try {
      sender.send(Collections.emptyList());
      return CheckResult.OK;
    } catch (Throwable t) {
      Call.propagateIfFatal(t);
      return CheckResult.failed(t);
    }
  }

  @Override public String toString() {
    return asyncReporter.toString().replace("AsyncReporter", "ForwarderStorage");
  }

  @Override public boolean isOverCapacity(Throwable e) {
    return super.isOverCapacity(e); // TODO
  }

  @Override public void close() {
    asyncReporter.close();
  }
}
