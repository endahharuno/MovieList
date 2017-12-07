package com.iakprojek.endah.movielist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.iakprojek.endah.movielist.adapter.TrailerAdapter;
import com.iakprojek.endah.movielist.api.Client;
import com.iakprojek.endah.movielist.api.Service;
import com.iakprojek.endah.movielist.data.FavoriteDbHelper;
import com.iakprojek.endah.movielist.model.Movie;
import com.iakprojek.endah.movielist.model.Trailer;
import com.iakprojek.endah.movielist.model.TrailerResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by endah on 27/11/17.
 */

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.title)
    TextView nameOfMovie;
    @BindView(R.id.plotsynopsis)
    TextView plotSynopsis;
    @BindView(R.id.userrating)
    TextView userRating;
    @BindView(R.id.releasedate)
    TextView releaseDate;
    @BindView(R.id.thumbnail_image_header)
    ImageView imageView;
    @BindView(R.id.thumbposter)
    ImageView thumbPoster;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    private RecyclerView recyclerView;
    private TrailerAdapter adapter;
    private List<Trailer> trailerList;

    private FavoriteDbHelper favoriteDbHelper;
    private Movie favorite;
    private final AppCompatActivity activity = DetailActivity.this;

    Movie movie;
    String thumbnail, movieName, synopsis, rating, dateOfRelease;
    int movie_id;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movies")) {
//            String thumbnail = getIntent().getExtras().getString("poster_path");
//            String movieName = getIntent().getExtras().getString("original_title");
//            String synopsis = getIntent().getExtras().getString("overview");
//            String rating = getIntent().getExtras().getString("vote_average");
//            String dateOfRelease = getIntent().getExtras().getString("release_date");

            movie = getIntent().getParcelableExtra("movies");

            thumbnail = movie.getPosterPath();
            movieName = movie.getOriginalTitle();
            synopsis = movie.getOverview();
            rating = Double.toString(movie.getVoteAverage());
            dateOfRelease = movie.getReleaseDate();
            movie_id = movie.getId();

            String poster = "http://image.tmdb.org/t/p/w500" + thumbnail;

            Glide.with(this)
                    .load(poster)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);

            Glide.with(this)
                    .load(poster)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(thumbPoster);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText("Rating : "+ rating);
            releaseDate.setText("Release : "+ dateOfRelease);

        } else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }

        MaterialFavoriteButton materialFavoriteButtonNice = findViewById(R.id.favorite_button);
//        materialFavoriteButtonNice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(activity, "endah jelek", Toast.LENGTH_SHORT).show();
//            }
//        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        materialFavoriteButtonNice.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            SharedPreferences.Editor editor = getSharedPreferences("com.iakprojek.endah.movielist.DetailActivity", MODE_PRIVATE).edit();
                            editor.putBoolean("Favorite added", true);
                            editor.commit();
                            saveFavorite();
                            Snackbar.make(buttonView, "Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            int movie_id = getIntent().getExtras().getInt("id");
                            favoriteDbHelper = new FavoriteDbHelper(DetailActivity.this);
                            favoriteDbHelper.deleteFavorite(movie_id);
                            Log.d("movie id", String.valueOf(movie_id));

                            SharedPreferences.Editor editor = getSharedPreferences("com.iakprojek.endah.movielist.DetailActivity", MODE_PRIVATE).edit();
                            editor.putBoolean("Favorite Removed", true);
                            editor.commit();
                            Snackbar.make(buttonView, "Remove from Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        initViews();
    }

    private void initCollapsingToolbar() {
        collapsingToolbarLayout.setTitle(" ");
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getIntent().getExtras().getString("original_title"));
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void initViews() {
        trailerList = new ArrayList<>();
        adapter = new TrailerAdapter(this, trailerList);

        recyclerView = findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        loadJSON();
    }

    private void loadJSON() {
//        int movie_id = getIntent().getExtras().getInt("id");
        try {
            if(BuildConfig.THE_MOVIE_API.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain your API key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client Client = new Client();
            Service apiService = com.iakprojek.endah.movielist.api.Client.getClient().create(Service.class);
            Call<TrailerResponse> call = apiService.getMovieTrailer(movie_id, BuildConfig.THE_MOVIE_API);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
//                    Log.d("endah", response.message());
                    List<Trailer> trailer = response.body().getResults();
                    recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailer));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFavorite() {
        favoriteDbHelper = new FavoriteDbHelper(activity);
        favorite = new Movie();
//        int movie_id = getIntent().getExtras ().getInt("movie_id");
//        String rate = getIntent().getExtras().getString("vote_average");
//        String poster = getIntent().getExtras().getString("poster_path");

        Double rate = movie.getVoteAverage();

        favorite.setId(movie_id);
        favorite.setOriginalTitle(movieName);
        favorite.setPosterPath(thumbnail);
        favorite.setVoteAverage(rate);
        favorite.setOverview(synopsis);

        favoriteDbHelper.addFavorite(favorite);
    }
}
