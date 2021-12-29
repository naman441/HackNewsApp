package com.growindigo.aimt.mynewsapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.growindigo.aimt.mynewsapp.dao.HeadlineDao;
import com.growindigo.aimt.mynewsapp.dao.WebHtmlDao;
import com.growindigo.aimt.mynewsapp.model.NewsHeadline;
import com.growindigo.aimt.mynewsapp.model.WebHtml;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * Database manager class to handle Room database functioning.
 * Implements Singleton behaviour to provide INSTANCE of db
 *
 */
@Database(entities = {NewsHeadline.class, WebHtml.class}, version = 1, exportSchema = false)
public abstract class DatabaseManager extends RoomDatabase {

    public abstract HeadlineDao headlineDao();
    public abstract WebHtmlDao webHtmlDao();

    private static volatile DatabaseManager INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static DatabaseManager getManagerDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (DatabaseManager.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseManager.class, "manager_db").build();
                }
            }
        }
        return INSTANCE;
    }
}
