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

import org.springframework.boot.context.properties.ConfigurationProperties;
import zipkin2.codec.Encoding;

@ConfigurationProperties("zipkin.storage.http-forwarder")
public class ZipkinHttpForwarderStorageProperties {
  String endpoint;
  Encoding encoding = Encoding.JSON;
  boolean compressionEnabled = false;
  Integer connectTimeout;
  Integer readTimeout;
  Integer messageMaxBytes;

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public Encoding getEncoding() {
    return encoding;
  }

  public void setEncoding(Encoding encoding) {
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
}
