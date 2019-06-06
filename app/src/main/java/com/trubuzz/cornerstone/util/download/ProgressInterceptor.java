package com.trubuzz.cornerstone.util.download;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class ProgressInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body()))
                .build();
    }
}
