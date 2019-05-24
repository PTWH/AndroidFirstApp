package com.example.myfirstapp.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {JSONData.class}, version = 3,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract JSONDataDao jsonDataDao();
}
