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
