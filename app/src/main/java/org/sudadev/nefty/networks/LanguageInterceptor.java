package org.sudadev.nefty.networks;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LanguageInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Accept-Language", Locale.getDefault().getLanguage());

        Request request = builder.build();
        return chain.proceed(request);
    }
}