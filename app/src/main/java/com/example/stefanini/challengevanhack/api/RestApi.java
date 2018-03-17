package com.example.stefanini.challengevanhack.api;

import android.content.Context;
import android.util.Log;

import com.example.stefanini.challengevanhack.util.Globals;
import com.example.stefanini.challengevanhack.util.SharedPrefs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Herson Rodrigues <hersonrodrigues@gmail.com> on 17/03/2018.
 */

public class RestApi {
    private static final String TAG = RestApi.class.getSimpleName();
    private static RestApi INSTANCE;
    private static String mToken;

    public static RestApi get() {
        INSTANCE = INSTANCE == null ? new RestApi() : INSTANCE;
        return INSTANCE;
    }

    private Retrofit getRetrofit(Context context) {
        if (mToken == null) {
            mToken = SharedPrefs.recoveryToken(context);
        }
        return getRetrofitBuilder();
    }

    private Retrofit getRetrofitBuilder() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        builder.retryOnConnectionFailure(true);

        builder.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.i(TAG, chain.request().url().toString());
                Request.Builder builder = chain.request().newBuilder();

                builder.addHeader("Authorization", "Bearer " + mToken);

                Request request = builder.build();
                return chain.proceed(request);
            }
        });

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Globals.URL_APP)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

    public IRestApi api(Context context) {
        return getRetrofit(context).create(IRestApi.class);
    }

    public static Call<Object> login(String email, String pass) {
        return getRetrofitSkipInstance().create(IRestApi.class).loginForm(email, pass);
    }

    private static Retrofit getRetrofitSkipInstance() {
        return new Retrofit.Builder()
                .baseUrl(Globals.URL_APP)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().setDateFormat("dd/MM/yyyy HH:mm:ss").create()))
                .build();
    }
}
