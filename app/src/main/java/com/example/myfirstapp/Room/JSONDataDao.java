package com.example.myfirstapp.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JSONDataDao {

    @Query("SELECT * FROM JSONData")
    List<JSONData> getAll();

    @Query("DELETE FROM JSONData")
    void deleteAll();

    @Delete
    void delete(JSONData jsonData);

    @Insert
    void insert(JSONData jsonData);
}
