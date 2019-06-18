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
package zipkin2.autoconfigure.storage.reporter;

import java.io.Serializable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import zipkin2.codec.Encoding;
import zipkin2.reporter.urlconnection.URLConnectionSender;
import zipkin2.storage.StorageComponent;
import zipkin2.storage.reporter.ZipkinURLConnectionReporterStorage;

@ConfigurationProperties("zipkin.storage.urlconnection-reporter")
public class ZipkinURLConnectionReporterStorageProperties implements Serializable {
  private static final long serialVersionUID = 0L;

  private String endpoint = "http://localhost:9411/api/v2/spans";

  private String encoding = Encoding.JSON.name();

  private boolean compressionEnabled = false;

  private int connectTimeout = 10 * 1000;

  private int readTimeout =  60 * 1000;

  private int messageMaxBytes = 5 * 1024 * 1024;

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public boolean isCompressionEnabled() {
    return compressionEnabled;
  }

  public void setCompressionEnabled(boolean compressionEnabled) {
    this.compressionEnabled = compressionEnabled;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public void setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public int getMessageMaxBytes() {
    return messageMaxBytes;
  }

  public void setMessageMaxBytes(int messageMaxBytes) {
    this.messageMaxBytes = messageMaxBytes;
  }

  StorageComponent.Builder toBuilder() {
    return ZipkinURLConnectionReporterStorage.newBuilder()
        .compressionEnabled(compressionEnabled)
        .connectTimeout(connectTimeout)
        .readTimeout(readTimeout)
        .endpoint(endpoint)
        .messageMaxBytes(messageMaxBytes)
        .encoding(encoding);
  }
}
