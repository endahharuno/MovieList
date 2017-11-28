package com.iakprojek.endah.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.thumb_image_header)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collaps_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("original_title")) {
            String thumbnail = getIntent().getExtras().getString("poster_path");
            String movieName = getIntent().getExtras().getString("original_title");
            String synopsis = getIntent().getExtras().getString("overview");
            String rating = getIntent().getExtras().getString("vote_average");
            String dateOfRelease = getIntent().getExtras().getString("release_date");

            Log.e("endah", "thumbnail = "+ thumbnail);
            Glide.with(this)
                    .load(thumbnail)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);
        } else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }
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
                    collapsingToolbarLayout.setTitle(getString(R.string.mov_details));
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


}
