package zipkin2.autoconfigure.storage.reporter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.storage.StorageComponent;
import zipkin2.storage.reporter.ZipkinReporterStorage;

@Configuration
@EnableConfigurationProperties(ZipkinReporterStorage.class)
@ConditionalOnProperty(name = "zipkin.storage.type", havingValue = "zipkin2/autoconfigure/storage/reporter")
@ConditionalOnMissingBean(StorageComponent.class)
public class ZipkinURLConnectionReporterStorageAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  StorageComponent storage(ZipkinURLConnectionReporterStorageProperties properties) {
    return properties.toBuilder().build();
  }
}
