package com.iakprojek.endah.movielist.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by endah on 27/11/17.
 */

public class ResponseMovies implements Parcelable {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeTypedList(this.results);
    }

    protected ResponseMovies(Parcel in) {
        this.page = in.readInt();
        this.results = in.createTypedArrayList(Movie.CREATOR);
    }

    public static final Parcelable.Creator<ResponseMovies> CREATOR = new Parcelable.Creator<ResponseMovies>() {
        @Override
        public ResponseMovies createFromParcel(Parcel source) {
            return new ResponseMovies(source);
        }

        @Override
        public ResponseMovies[] newArray(int size) {
            return new ResponseMovies[size];
        }
    };
}
