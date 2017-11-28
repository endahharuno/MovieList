package com.iakprojek.endah.movielist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.iakprojek.endah.movielist.adapter.MoviesAdapter;
import com.iakprojek.endah.movielist.api.Client;
import com.iakprojek.endah.movielist.api.Service;
import com.iakprojek.endah.movielist.model.Movie;
import com.iakprojek.endah.movielist.model.ResponseMovies;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rec_view)
    RecyclerView recyclerView;
    @BindView(R.id.main_content)
    SwipeRefreshLayout swipeContainer;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
    public static final String LOG_TAG = MoviesAdapter.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initViews();

        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(MainActivity.this, "Movie Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void initViews() {
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching movies ...");
        pd.setCancelable(false);
        pd.show();

        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        loadJSON();
    }

    private void loadJSON() {

        try {
            if (BuildConfig.THE_MOVIE_API.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain API Key firstly from themoviedb.org", Toast.LENGTH_SHORT);
                pd.dismiss();
                return;
            }

            Client Client = new Client();
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<ResponseMovies> call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_API);
            call.enqueue(new Callback<ResponseMovies>() {
                @Override
                public void onResponse(Call<ResponseMovies> call, Response<ResponseMovies> response) {
                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseMovies> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
