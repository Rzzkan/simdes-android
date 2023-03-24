package com.simdes.appgo.network;

import android.content.Context;
import android.util.Log;

import com.simdes.appgo.BuildConfig;
import com.simdes.appgo.utils.UserHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit getClient(Context context) {
        // Create the Client
        OkHttpClient client;
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor addNetworkInterceptor = chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer "+ UserHelper.api_token);
                Log.e("TOKEN",  UserHelper.api_token);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            };

            client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(addNetworkInterceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        } else {
            Interceptor addNetworkInterceptor = chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer "+UserHelper.api_token);
                Log.e("TOKEN",  UserHelper.api_token);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            };

            client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(addNetworkInterceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }

        // Initialize retrofit instance

        return new Retrofit.Builder()
                .baseUrl(Config.BASE_API)
                .client(client)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClient2(Context context) {
        // Create the Client
        OkHttpClient client;
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        } else {

            client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }

        // Initialize retrofit instance

        return new Retrofit.Builder()
                .baseUrl(Config.BASE_API)
                .client(client)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
