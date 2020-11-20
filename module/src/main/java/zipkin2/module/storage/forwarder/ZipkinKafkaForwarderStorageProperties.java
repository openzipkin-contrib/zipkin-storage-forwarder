/*
 * Copyright 2019-2020 The OpenZipkin Authors
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
package zipkin2.module.storage.forwarder;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import zipkin2.codec.Encoding;

@ConfigurationProperties("zipkin.storage.kafka-forwarder")
public class ZipkinKafkaForwarderStorageProperties {
  String bootstrapServers;
  String topic;
  Map<String, String> overrides = new LinkedHashMap<>();
  Encoding encoding = Encoding.PROTO3;
  Integer messageMaxBytes;

  public String getBootstrapServers() {
    return bootstrapServers;
  }

  public void setBootstrapServers(String bootstrapServers) {
    this.bootstrapServers = bootstrapServers;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public Encoding getEncoding() {
    return encoding;
  }

  public void setEncoding(Encoding encoding) {
    this.encoding = encoding;
  }

  public Map<String, String> getOverrides() {
    return overrides;
  }

  public void setOverrides(Map<String, String> overrides) {
    this.overrides = overrides;
  }

  public int getMessageMaxBytes() {
    return messageMaxBytes;
  }

  public void setMessageMaxBytes(int messageMaxBytes) {
    this.messageMaxBytes = messageMaxBytes;
  }
}
