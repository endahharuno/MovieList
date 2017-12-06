package com.iakprojek.endah.movielist.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by endah on 27/11/17.
 */

public class Client {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if(retrofit == null) {
                        HttpLoggingInterceptor mInterceptor = new HttpLoggingInterceptor();
            mInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient mClient = new OkHttpClient.Builder()
                    .addInterceptor(mInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(mClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
