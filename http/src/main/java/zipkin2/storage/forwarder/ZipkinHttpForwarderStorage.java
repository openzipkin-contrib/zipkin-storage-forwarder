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

import zipkin2.reporter.okhttp3.OkHttpSender;
import zipkin2.storage.StorageComponent;

public class ZipkinHttpForwarderStorage extends ZipkinForwarderStorage<OkHttpSender> {

  ZipkinHttpForwarderStorage(Builder builder) {
    super(builder);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder extends ZipkinForwarderStorage.Builder<OkHttpSender> {
    final OkHttpSender.Builder delegate = OkHttpSender.newBuilder();

    public Builder endpoint(String endpoint) {
      this.delegate.endpoint(endpoint);
      return this;
    }

    public Builder compressionEnabled(boolean compressionEnabled) {
      this.delegate.compressionEnabled(compressionEnabled);
      return this;
    }

    public Builder connectTimeout(int connectTimeout) {
      this.delegate.connectTimeout(connectTimeout);
      return this;
    }

    public Builder readTimeout(int readTimeout) {
      this.delegate.readTimeout(readTimeout);
      return this;
    }

    @Override public StorageComponent build() {
      return new ZipkinHttpForwarderStorage(this);
    }

    @Override
    OkHttpSender sender() {
      if (messageMaxBytes != null) this.delegate.messageMaxBytes(messageMaxBytes);
      return this.delegate.encoding(encoding).build();
    }
  }
}
