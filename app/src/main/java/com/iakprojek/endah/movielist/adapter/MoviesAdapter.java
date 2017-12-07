package com.iakprojek.endah.movielist.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iakprojek.endah.movielist.DetailActivity;
import com.iakprojek.endah.movielist.R;
import com.iakprojek.endah.movielist.model.Movie;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by endah on 27/11/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Movie> movieList;

    public MoviesAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int i) {
        viewHolder.title.setText(movieList.get(i).getOriginalTitle());

        viewHolder.userrating.setText(movieList.get(i).getVoteAverage() +"");
        viewHolder.ratingBar.setRating((float) (movieList.get(i).getVoteAverage()/2.0));
        String poster = "http://image.tmdb.org/t/p/w500" + movieList.get(i).getPosterPath();

        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.ic_launcher_background)
                .into(viewHolder.thumb);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.userrating)
        TextView userrating;
        @BindView(R.id.thumbnail)
        ImageView thumb;
        @BindView(R.id.ratingbar)
        RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
//                        Movie clickedDataItem = movieList.get(pos);
//                        Intent intent = new Intent(mContext, DetailActivity.class);

//                        intent.putExtra("original_title", movieList.get(pos).getOriginalTitle());
//                        intent.putExtra("poster_path", movieList.get(pos).getPosterPath());
//                        intent.putExtra("overview", movieList.get(pos).getOverview());
//                        intent.putExtra("vote_average", Double.toString(movieList.get(pos).getVoteAverage()));
//                        intent.putExtra("release_date", movieList.get(pos).getReleaseDate());
//                        intent.putExtra("movie_id", movieList.get(pos).getId());
                        Movie clickedDataItem = movieList.get(pos);
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("movies", clickedDataItem);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getOriginalTitle(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
