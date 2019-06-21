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
package zipkin2.autoconfigure.storage.forwarder;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.storage.StorageComponent;

@Configuration
@EnableConfigurationProperties(ZipkinKafkaForwarderStorageProperties.class)
@ConditionalOnProperty(name = "zipkin.storage.type", havingValue = "kafka-forwarder")
@ConditionalOnMissingBean(StorageComponent.class)
public class ZipkinKafkaForwarderStorageAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  StorageComponent storage(ZipkinKafkaForwarderStorageProperties properties) {
    return properties.toBuilder().build();
  }
}