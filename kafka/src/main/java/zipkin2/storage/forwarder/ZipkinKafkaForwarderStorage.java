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

import java.util.Map;

import zipkin2.codec.Encoding;
import zipkin2.reporter.kafka11.KafkaSender;
import zipkin2.storage.StorageComponent;

public class ZipkinKafkaForwarderStorage extends ZipkinForwarderStorage<KafkaSender> {
  ZipkinKafkaForwarderStorage(Builder builder) {
    super(builder);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder extends ZipkinForwarderStorage.Builder<KafkaSender> {
    final KafkaSender.Builder delegate = KafkaSender.newBuilder();

    public Builder topic(String topic) {
      this.delegate.topic(topic);
      return this;
    }

    public Builder bootstrapServers(String bootstrapServers) {
      this.delegate.bootstrapServers(bootstrapServers);
      return this;
    }

    public Builder overrides(Map<String, ?> overrides) {
      this.delegate.overrides(overrides);
      return this;
    }

    @Override public StorageComponent build() {
      if (messageMaxBytes != null) this.delegate.messageMaxBytes(messageMaxBytes);
      this.sender = this.delegate.encoding(encoding).build();
      return new ZipkinKafkaForwarderStorage(this);
    }
  }
}
