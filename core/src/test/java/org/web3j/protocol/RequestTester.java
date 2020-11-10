/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.junit.jupiter.api.BeforeEach;

import org.web3j.protocol.http.HttpService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class RequestTester {

    private OkHttpClient httpClient;
    private HttpService httpService;
    public static final MediaType JSON_MEDIA_TYPE =
            MediaType.parse("application/json; charset=utf-8");
    private RequestInterceptor requestInterceptor;

    @BeforeEach
    public void setUp() {
        requestInterceptor = new RequestInterceptor();
        httpClient = new OkHttpClient.Builder().addInterceptor(requestInterceptor).build();
        httpService = new HttpService(httpClient);
        initWeb3Client(httpService);
    }

    protected abstract void initWeb3Client(HttpService httpService);

    protected void verifyResult(String expected) throws Exception {
        RequestBody requestBody = requestInterceptor.getRequestBody();
        assertNotNull(requestBody);
        assertEquals(requestBody.contentType(), (HttpService.JSON_MEDIA_TYPE));

        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        assertEquals((replaceRequestId(expected)), replaceRequestId(buffer.readUtf8()));
    }

    private String replaceRequestId(String json) {
        return json.replaceAll("\"id\":\\d*}$", "\"id\":<generatedValue>}");
    }

    private class RequestInterceptor implements Interceptor {

        private RequestBody requestBody;

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            this.requestBody = request.body();
            okhttp3.Response response =
                    new okhttp3.Response.Builder()
                            .request(chain.request())
                            .protocol(Protocol.HTTP_2)
                            .code(200)
                            .body(
                                    ResponseBody.create(
                                            "{\"jsonrpc\":\"2.0\",\"method\":\"\","
                                                    + "\"params\":[],\"id\":1}",
                                            JSON_MEDIA_TYPE))
                            .message("")
                            .build();

            return response;
        }

        public RequestBody getRequestBody() {
            return requestBody;
        }
    }
}
