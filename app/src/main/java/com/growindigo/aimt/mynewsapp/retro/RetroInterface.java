package com.growindigo.aimt.mynewsapp.retro;

import com.growindigo.aimt.mynewsapp.model.RetroResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetroInterface {

    // fetched top stories ID
    @GET("topstories.json?print=pretty")
    Call<List<Integer>> getTopStories();

    // fetches news from ID
    @GET("item/{newsId}.json?print=pretty")
    Call<RetroResponse> getArticle(@Path("newsId") int id);
}
