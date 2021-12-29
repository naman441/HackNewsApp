package com.growindigo.aimt.mynewsapp.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.growindigo.aimt.mynewsapp.DatabaseManager;
import com.growindigo.aimt.mynewsapp.dao.HeadlineDao;
import com.growindigo.aimt.mynewsapp.model.NewsHeadline;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class HeadlineRepo {

    private final HeadlineDao dao;
    private LiveData<List<NewsHeadline>> list;
    private int count;

    // get dao object from Db manager
    public HeadlineRepo(Context context){
        DatabaseManager db = DatabaseManager.getManagerDatabase(context);
        dao = db.headlineDao();
    }

    // fetch count to check list in db
    public int fetchListCount(){
        try {
            DatabaseManager.databaseWriteExecutor.submit(()->
                count = dao.getNewsCount()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return count;
    }

    // filter list on search
    public LiveData<List<NewsHeadline>> fetchListFromFilter(String filter){
        try {
            DatabaseManager.databaseWriteExecutor.submit(()->
                list = dao.getFilterList(filter)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    // insert new news title
    public void insertHeadline(NewsHeadline headline){
        DatabaseManager.databaseWriteExecutor.execute(()->
            dao.insert(headline));
    }

    // fetch news with limit set
    public LiveData<List<NewsHeadline>> fetchLimitedNews(int value){
        try {
            DatabaseManager.databaseWriteExecutor.submit(()->
                list = dao.getLimitNews(value)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    // get news id to check if exists in db already
    public int getNewsId(int id){
        try {
            DatabaseManager.databaseWriteExecutor.submit(()->{
                count = dao.getNewsId(id);
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return count;
    }

    // update news status to read
    public void updateStatus(int status, int newsId){
        DatabaseManager.databaseWriteExecutor.execute(()->
            dao.update(status, newsId));
    }

}
