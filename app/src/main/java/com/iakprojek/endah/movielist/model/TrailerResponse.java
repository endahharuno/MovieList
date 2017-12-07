package com.iakprojek.endah.movielist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by endah on 04/12/17.
 */

public class TrailerResponse {

    @SerializedName("id")
    private int id_trailer;
    @SerializedName("results")
    private List<Trailer> results;

    public int getIdTrailer() {
        return id_trailer;
    }

    public void setIdTrailer(int id_trailer) {
        this.id_trailer = id_trailer;
    }

    public List<Trailer> getResults() {
        return results;
    }
}
