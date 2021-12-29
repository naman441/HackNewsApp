package com.growindigo.aimt.mynewsapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.growindigo.aimt.mynewsapp.model.WebHtml;

/**
 * dao for page caching
 */
@Dao
public interface WebHtmlDao extends BaseDao<WebHtml> {

    // fetch news
    @Query("select html from webhtml where newsId = :newsId")
    String fetchNewsHtml(int newsId);

    // return inserted id to validate page save
    @Insert
    long insertWithReturn(WebHtml html);

}
