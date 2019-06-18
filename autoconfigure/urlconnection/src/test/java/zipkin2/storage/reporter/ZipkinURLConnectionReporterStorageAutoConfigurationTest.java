package zipkin2.storage.reporter;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import zipkin2.autoconfure.storage.reporter.Access;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipkinURLConnectionReporterStorageAutoConfigurationTest {
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
        context.getBean(ZipkinURLConnectionReporterStorage.class);
    }

    @Test public void providesStorageComponent_whenStorageTypeCorrect() {
        context = new AnnotationConfigApplicationContext();
        TestPropertyValues.of(
                "zipkin.storage.type:urlconnection-reporter",
                "zipkin.storage.urlconnection-reporter.endpoint:http://localhost2:9411/api/v2/spans"
        ).applyTo(context);
        Access.registerReporter(context);
        context.refresh();

        assertThat(context.getBean(ZipkinURLConnectionReporterStorage.class)).isNotNull();
    }
}