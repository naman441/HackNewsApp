package com.growindigo.aimt.mynewsapp.retro;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroBuilder {

    public static final String Base_URL="https://hacker-news.firebaseio.com/v0/";
    private static Retrofit retrofit = null;
    private static final int TIME_OUT = 30;

    // get retro client to call service
    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (retrofit==null) {
            OkHttpClient ok=new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(ok.newBuilder().connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .build()).build();
        }
        return retrofit;
    }
}
