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
package zipkin2.storage.forwarder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import zipkin2.module.storage.forwarder.Access;
import zipkin2.storage.StorageComponent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZipkinForwarderStorageModuleTest {
  AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

  @AfterEach void close() {
    context.close();
  }

  @Test void doesNotProvideStorageComponent_whenStorageTypeNotHttp() {
    TestPropertyValues.of("zipkin.storage.type:elasticsearch").applyTo(context);
    Access.registerModule(context);
    context.refresh();

    assertThatThrownBy(() -> context.getBean(StorageComponent.class))
      .isInstanceOf(NoSuchBeanDefinitionException.class);
  }

  @Test void providesStorageComponent_whenStorageTypeHttp() {
    TestPropertyValues.of(
      "zipkin.storage.type:http-forwarder",
      "zipkin.storage.http-forwarder.endpoint:http://localhost2:9411/api/v2/spans"
    ).applyTo(context);
    Access.registerModule(context);
    context.refresh();

    assertThat(context.getBean(StorageComponent.class)).isNotNull();
  }

  @Test void providesStorageComponent_whenStorageTypeKafka() {
    TestPropertyValues.of(
      "zipkin.storage.type:kafka-forwarder",
      "zipkin.storage.kafka-forwarder.bootstrap-servers:localhost:9092"
    ).applyTo(context);
    Access.registerModule(context);
    context.refresh();

    assertThat(context.getBean(StorageComponent.class)).isNotNull();
  }
}
