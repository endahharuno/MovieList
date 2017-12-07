package com.iakprojek.endah.movielist.adapter;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iakprojek.endah.movielist.R;
import com.iakprojek.endah.movielist.model.Trailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by endah on 04/12/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Trailer> trailerList;

    public TrailerAdapter(Context mContext, List<Trailer> trailerList) {
        this.mContext = mContext;
        this.trailerList = trailerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.MyViewHolder viewHolder, int i) {
        viewHolder.title.setText(trailerList.get(i).getName());
//        setOnClickItem(viewHolder, i);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }


//    private void setOnClickItem(MyViewHolder viewHolder,final int pos) {
//        viewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                final Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerList.get(pos).getKey()));
////                final Intent webIntent = new Intent(Intent.ACTION_VIEW,
////                        Uri.parse("http://www.youtube.com/watch?v=" + trailerList.get(pos).getKey()));
//                String result = trailerList.get(pos).getKey();
//                Toast.makeText(mContext, "result " + result, Toast.LENGTH_SHORT).show();
////                new AlertDialog.Builder(mContext)
////                        .setMessage("Play this video on Youtube ?")
////                        .setCancelable(false)
////                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int id) {
////                                try {
////                                    mContext.startActivity(appIntent);
////                                } catch (ActivityNotFoundException ex) {
////                                    mContext.startActivity(webIntent);
////                                }
////                            }
////                        })
////                        .setNegativeButton("Cancel", null)
////                        .show();
//            }
//        });
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.imageView)
        ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Trailer clickedDataItem = trailerList.get(pos);
                        String videoId = trailerList.get(pos).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+videoId));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("VIDEO_ID", videoId);
                        mContext.startActivity(intent);

                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
