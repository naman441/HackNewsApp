package com.growindigo.aimt.mynewsapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.growindigo.aimt.mynewsapp.model.NewsHeadline;

import java.util.List;

/**
 * dao for news title entity
 */
@Dao
public interface HeadlineDao extends BaseDao<NewsHeadline>{

    // get count of news in db
    @Query("select count(*) from newsheadline")
    int getNewsCount();

    // get all news
    @Query("select * from newsheadline")
    LiveData<List<NewsHeadline>> getAllNews();

    // get all news with limit of 50
    @Query("select * from newsheadline order by id desc limit :value")
    LiveData<List<NewsHeadline>> getLimitNews(int value);

    // get newsId for checking if news already cached in Db
    @Query("select newsId from newsheadline where newsId = :id")
    int getNewsId(int id);

    // search query
    @Query("select * from newsheadline where title like '%' || :filter || '%'")
    LiveData<List<NewsHeadline>> getFilterList(String filter);

    // update status of page stored in db
    @Query("update newsheadline set status = :status where newsId = :newsId")
    void update(int status, int newsId);
}
