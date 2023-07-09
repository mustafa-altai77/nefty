package org.sudadev.nefty.networks;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.sudadev.nefty.BuildConfig;
import org.sudadev.nefty.common.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static final String API_BASE_URL = Constants.API_URL;

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(GSON));

    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static LanguageInterceptor languageInterceptor = new LanguageInterceptor();

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();

        httpClient.connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(languageInterceptor);

        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(logging);
        }

        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            httpClient.addInterceptor(interceptor);
        }

        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    public static Retrofit retrofit() {
        return retrofit;
    }
}
