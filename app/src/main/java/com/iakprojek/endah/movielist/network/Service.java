package com.iakprojek.endah.movielist.network;

import com.iakprojek.endah.movielist.model.ResponseMovies;
import com.iakprojek.endah.movielist.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by endah on 27/11/17.
 */

public interface Service {

    @GET("movie/popular")
    Call<ResponseMovies> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<ResponseMovies> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);
}
