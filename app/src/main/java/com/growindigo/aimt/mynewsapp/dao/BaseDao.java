package com.growindigo.aimt.mynewsapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

/**
 * interface for basic operations
 * @param <T> - pass entity class here
 *
 */
@Dao
public interface BaseDao<T> {

        @Insert
        void insert(T obj);

        @Insert
        void insert(List<T> obj);

        @Update
        void update(T obj);

        @Delete
        void delete(T obj);

}
