package com.iakprojek.endah.movielist.api;

import com.iakprojek.endah.movielist.model.ResponseMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by endah on 27/11/17.
 */

public interface Service {

    @GET("movie/popular")
    Call<ResponseMovies> getPopularMovies(@Query("api_key") String apKey);

    @GET("movie/top_rated")
    Call<ResponseMovies> getTopRatedMovies(@Query("api_key") String apKey);
}
