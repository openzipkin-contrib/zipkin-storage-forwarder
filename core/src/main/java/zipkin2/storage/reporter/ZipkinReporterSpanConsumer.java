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
package zipkin2.storage.reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import zipkin2.Call;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.codec.SpanBytesEncoder;
import zipkin2.reporter.Sender;
import zipkin2.storage.SpanConsumer;

public class ZipkinReporterSpanConsumer<S extends Sender> implements SpanConsumer {

  final S sender;
  final SpanBytesEncoder encoder;

  ZipkinReporterSpanConsumer(ZipkinReporterStorage<S> storage) {
    this.sender = storage.sender;
    this.encoder = storage.encoder;
  }

  @Override public Call<Void> accept(List<Span> spans) {
    List<byte[]> encodedList = new ArrayList<>(spans.size());
    for (Span span: spans) {
      encodedList.add(encoder.encode(span));
    }
    return sender.sendSpans(encodedList);
  }
}
