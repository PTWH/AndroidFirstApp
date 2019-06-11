package com.example.myfirstapp.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TokenDataDao {
    @Query("DELETE FROM TokenData")
    void deleteAll();

    @Insert
    void insert(TokenData tokenData);
}
