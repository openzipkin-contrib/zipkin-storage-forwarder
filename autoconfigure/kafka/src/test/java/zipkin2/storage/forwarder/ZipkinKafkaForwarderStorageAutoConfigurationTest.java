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

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import zipkin2.autoconfure.storage.reporter.Access;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipkinKafkaForwarderStorageAutoConfigurationTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    AnnotationConfigApplicationContext context;

    @After
    public void close() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    public void doesntProvidesStorageComponent_whenStorageTypeNotCorrect() {
        context = new AnnotationConfigApplicationContext();
        TestPropertyValues.of("zipkin.storage.type:elasticsearch").applyTo(context);
        Access.registerReporter(context);
        context.refresh();

        thrown.expect(NoSuchBeanDefinitionException.class);
        context.getBean(ZipkinKafkaForwarderStorage.class);
    }

    @Test public void providesStorageComponent_whenStorageTypeCorrect() {
        context = new AnnotationConfigApplicationContext();
        TestPropertyValues.of(
                "zipkin.storage.type:kafka-forwarder",
                "zipkin.storage.forwarder.kafka.bootstrap-servers:localhost:9092"
        ).applyTo(context);
        Access.registerReporter(context);
        context.refresh();

        assertThat(context.getBean(ZipkinKafkaForwarderStorage.class)).isNotNull();
    }
}