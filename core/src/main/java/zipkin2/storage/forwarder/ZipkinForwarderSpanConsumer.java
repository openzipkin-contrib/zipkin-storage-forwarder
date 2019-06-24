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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zipkin2.Call;
import zipkin2.Callback;
import zipkin2.Span;
import zipkin2.codec.SpanBytesEncoder;
import zipkin2.internal.AggregateCall;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.storage.SpanConsumer;

public class ZipkinForwarderSpanConsumer<S extends Sender> implements SpanConsumer {

  final AsyncReporter<Span> reporter;

  ZipkinForwarderSpanConsumer(ZipkinForwarderStorage<S> storage) {
    this.reporter = storage.reporter;
  }

  @Override public Call<Void> accept(List<Span> spans) {
    List<Call<Void>> calls = new ArrayList<>(spans.size());
    for (Span span: spans) {
      calls.add(new ReporterCall(reporter, span));
    }
    return AggregateCall.newVoidCall(calls);
  }

  static class ReporterCall extends Call<Void> {
    final AsyncReporter<Span> reporter;
    final Span span;

    ReporterCall(AsyncReporter<Span> reporter, Span span) {
      this.reporter = reporter;
      this.span = span;
    }

    @Override
    public Void execute() throws IOException {
      reporter.report(span);
      return null;
    }

    @Override
    public void enqueue(Callback<Void> callback) {
      reporter.report(span);
      callback.onSuccess(null); // not much we can do at this level of abstraction
    }

    @Override
    public void cancel() {
    }

    @Override
    public boolean isCanceled() {
      return false;
    }

    @Override
    public Call<Void> clone() {
      return new ReporterCall(reporter, span);
    }
  }
}
