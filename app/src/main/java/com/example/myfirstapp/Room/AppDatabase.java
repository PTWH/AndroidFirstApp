package com.example.myfirstapp.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {JSONData.class,TokenData.class}, version = 4,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract JSONDataDao jsonDataDao();
    public abstract TokenDataDao tokenDataDao();
}
