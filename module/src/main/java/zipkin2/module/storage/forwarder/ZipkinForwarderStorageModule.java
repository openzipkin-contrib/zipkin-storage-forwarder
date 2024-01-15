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
package zipkin2.module.storage.forwarder;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.BytesMessageSender;
import zipkin2.reporter.kafka.KafkaSender;
import zipkin2.reporter.okhttp3.OkHttpSender;
import zipkin2.storage.StorageComponent;
import zipkin2.storage.forwarder.ForwarderStorage;

@Configuration
@EnableConfigurationProperties({
  ZipkinHttpForwarderStorageProperties.class,
  ZipkinKafkaForwarderStorageProperties.class,
})
@ConditionalOnMissingBean(StorageComponent.class)
public class ZipkinForwarderStorageModule {

  @ConditionalOnProperty(name = "zipkin.storage.type", havingValue = "http-forwarder")
  @Bean OkHttpSender okHttpSender(ZipkinHttpForwarderStorageProperties properties) {
    OkHttpSender.Builder builder = OkHttpSender.newBuilder();
    if (properties.endpoint != null) builder.endpoint(properties.endpoint);
    if (properties.connectTimeout != null) builder.connectTimeout(properties.connectTimeout);
    if (properties.readTimeout != null) builder.readTimeout(properties.readTimeout);
    if (properties.messageMaxBytes != null) builder.messageMaxBytes(properties.messageMaxBytes);
    if (properties.compressionEnabled) builder.compressionEnabled(true);
    if (properties.encoding != null) builder.encoding(properties.encoding);
    return builder.build();
  }

  @ConditionalOnProperty(name = "zipkin.storage.type", havingValue = "kafka-forwarder")
  @Bean KafkaSender kafkaSender(ZipkinKafkaForwarderStorageProperties properties) {
    KafkaSender.Builder builder = KafkaSender.newBuilder();
    if (properties.bootstrapServers != null) builder.bootstrapServers(properties.bootstrapServers);
    if (properties.topic != null) builder.topic(properties.topic);
    if (properties.overrides != null) builder.overrides(properties.overrides);
    if (properties.messageMaxBytes != null) builder.messageMaxBytes(properties.messageMaxBytes);
    if (properties.encoding != null) builder.encoding(properties.encoding);
    return builder.build();
  }

  @ConditionalOnBean(BytesMessageSender.class)
  @Bean StorageComponent storage(BytesMessageSender sender) {
    return ForwarderStorage.newBuilder(sender).build();
  }
}
