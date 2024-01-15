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

import java.util.List;
import zipkin2.Call;
import zipkin2.Callback;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.storage.SpanConsumer;

/** Forwards a list of {@linkplain Span spans} to {@link AsyncReporter#report(Object)}. */
record ForwarderSpanConsumer(AsyncReporter<Span> asyncReporter) implements SpanConsumer {
  @Override public Call<Void> accept(List<Span> spans) {
    if (spans.isEmpty()) return Call.create(null);
    return new ReporterCall(asyncReporter, spans);
  }

  static final class ReporterCall extends Call.Base<Void> {
    final AsyncReporter<Span> asyncReporter;
    final List<Span> spans;

    ReporterCall(AsyncReporter<Span> asyncReporter, List<Span> spans) {
      this.asyncReporter = asyncReporter;
      this.spans = spans;
    }

    @Override public String toString() {
      return "ReporterCall{" + spans + "}";
    }

    @Override protected Void doExecute() {
      for (Span span : spans) asyncReporter.report(span);
      asyncReporter.flush();
      return null;
    }

    @Override public void doEnqueue(Callback<Void> callback) {
      try {
        for (Span span : spans) asyncReporter.report(span);
        callback.onSuccess(null);
      } catch (Throwable t) {
        propagateIfFatal(t);
        callback.onError(t);
      }
    }

    @Override public ReporterCall clone() {
      return new ReporterCall(asyncReporter, spans);
    }
  }
}
