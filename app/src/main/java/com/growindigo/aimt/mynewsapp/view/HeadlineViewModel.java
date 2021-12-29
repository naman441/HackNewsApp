package com.growindigo.aimt.mynewsapp.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.growindigo.aimt.mynewsapp.middle.NewsHandler;
import com.growindigo.aimt.mynewsapp.model.NewsHeadline;

import java.util.List;
import java.util.stream.Collectors;

public class HeadlineViewModel extends AndroidViewModel {

    private MutableLiveData<List<NewsHeadline>> list;
    private NewsHandler handler;
    private MutableLiveData<String> filter = new MutableLiveData<>();

    public HeadlineViewModel(@NonNull Application application) {
        super(application);
        handler = new NewsHandler(application.getApplicationContext());
    }

    public void setFilter(String value){
        filter.setValue(value);
    }

    public LiveData<List<NewsHeadline>> getList(){
        return Transformations.switchMap(filter, v-> handler.getNewsList(v));
    }

    public LiveData<List<NewsHeadline>> refreshList(){
        return handler.getNewsListFromRetro();
    }


}
