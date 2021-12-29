package com.growindigo.aimt.mynewsapp.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = NewsHeadline.class, parentColumns = "newsId", childColumns = "newsId", onDelete = CASCADE)})
public class WebHtml {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int newsId;

    private String html;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
