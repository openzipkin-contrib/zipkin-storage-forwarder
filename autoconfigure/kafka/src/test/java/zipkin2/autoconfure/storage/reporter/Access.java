package zipkin2.autoconfure.storage.reporter;

import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import zipkin2.autoconfigure.storage.reporter.ZipkinKafkaReporterStorageAutoConfiguration;

public class Access {

    public static void registerReporter(AnnotationConfigApplicationContext context) {
        context.register(PropertyPlaceholderAutoConfiguration.class, ZipkinKafkaReporterStorageAutoConfiguration.class);
    }
}
