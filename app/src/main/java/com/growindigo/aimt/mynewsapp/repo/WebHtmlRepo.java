package com.growindigo.aimt.mynewsapp.repo;

import android.content.Context;

import com.growindigo.aimt.mynewsapp.DatabaseManager;
import com.growindigo.aimt.mynewsapp.dao.WebHtmlDao;
import com.growindigo.aimt.mynewsapp.model.WebHtml;

import java.util.concurrent.ExecutionException;

public class WebHtmlRepo {

    private final WebHtmlDao dao;
    private String newsHtml;
    private long value;

    public WebHtmlRepo(Context context){
        DatabaseManager manager = DatabaseManager.getManagerDatabase(context);
        dao = manager.webHtmlDao();
    }

    // insert page in db
    public long insert(WebHtml html){
        try{
            DatabaseManager.databaseWriteExecutor.submit(()->
                value =  dao.insertWithReturn(html)).get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

    // get page from db
    public String retrieveHtml(int newsId){
        try {
            DatabaseManager.databaseWriteExecutor.submit(()->
                newsHtml = dao.fetchNewsHtml(newsId)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return newsHtml;
    }
}
