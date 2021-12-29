package com.growindigo.aimt.mynewsapp.middle;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.growindigo.aimt.mynewsapp.AppUtils;
import com.growindigo.aimt.mynewsapp.model.NewsHeadline;
import com.growindigo.aimt.mynewsapp.model.RetroResponse;
import com.growindigo.aimt.mynewsapp.model.WebHtml;
import com.growindigo.aimt.mynewsapp.repo.HeadlineRepo;
import com.growindigo.aimt.mynewsapp.repo.WebHtmlRepo;
import com.growindigo.aimt.mynewsapp.retro.RetroBuilder;
import com.growindigo.aimt.mynewsapp.retro.RetroInterface;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles all backend aspects
 * Saves Retro data to db for offline list
 * Retrieves data from DB first, if not available, uses Retro
 */
public class NewsHandler {

    private final HeadlineRepo repo;
    private final WebHtmlRepo webRepo;

    public NewsHandler(Context context){
        repo = new HeadlineRepo(context);
        webRepo = new WebHtmlRepo(context);
    }

    // fetch news using filter if value is not empty/null - used for search criteria
    // else fetch a general list
    public LiveData<List<NewsHeadline>> getNewsList(String value){
        LiveData<List<NewsHeadline>> list;
        if(value != null && !value.isEmpty()){
            list = repo.fetchListFromFilter(value);
        }
        else {
            int count = repo.fetchListCount();
            if (count < 50)
                list = getNewsListFromRetro();
            else
                list = getNewsListFromDb();
        }
        return list;
    }

    // get list from retro and save it to Db
    public LiveData<List<NewsHeadline>> getNewsListFromRetro(){
        hitRetro();
        return getNewsListFromDb();
    }

    // limit set to 50 - can be retrieved from settings (SP)
    private LiveData<List<NewsHeadline>> getNewsListFromDb(){
        return repo.fetchLimitedNews(50);
    }

    // insert only in case news in not in Db already
    // used when refreshing list
    private void saveHeadlineToDb(NewsHeadline headline){
        try{
            if(repo.getNewsId(headline.getNewsId()) == 0)
                repo.insertHeadline(headline);
        } catch (Exception se){
            se.printStackTrace();
        }
    }

    private void hitRetro(){
        final RetroInterface apiInterface =
                RetroBuilder.getClient().create(RetroInterface.class);
        apiInterface.getTopStories().enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<List<Integer>> call, @NonNull Response<List<Integer>> response) {
                //gets list of stories ID
                List<Integer> topStories = response.body();
                // iterate over list of stories ID
                for (int i = 0; i < 50; i++) {
                    assert topStories != null;
                    apiInterface.getArticle(topStories.get(i)).enqueue(new Callback<RetroResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<RetroResponse> call, @NonNull Response<RetroResponse> response) {
                            if(response.body() != null){
                                NewsHeadline head = new NewsHeadline();
                                head.setNewsId(response.body().getId());
                                head.setBy(response.body().getBy());
                                head.setTime(AppUtils.getTime(response.body().getTime()));
                                head.setTitle(response.body().getTitle());
                                head.setUrl(response.body().getUrl());
                                saveHeadlineToDb(head);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<RetroResponse> call, @NonNull Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Integer>> call, @NonNull Throwable t) {

            }
        });
    }

    // save html text to db, update page status as read
    public void saveHtmlToDb(WebHtml html){
        if(getHtmlPageFromDb(html.getNewsId()) == null){
            long value = webRepo.insert(html);
            if(value > 0)
                repo.updateStatus(1, html.getNewsId());
        }

    }

    public String getHtmlPageFromDb(int newsId){
        return webRepo.retrieveHtml(newsId);
    }
}
