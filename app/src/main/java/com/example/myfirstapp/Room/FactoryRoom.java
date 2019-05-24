package com.example.myfirstapp.Room;

import android.content.Context;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class FactoryRoom {

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, `name` TEXT, PRIMARY KEY(`id`))");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE JSONData ADD COLUMN JS_URL VARCHAR");
        }
    };


    public static AppDatabase getInstance(Context ctx) {
        AppDatabase db = Room.databaseBuilder(
                ctx.getApplicationContext(),
                AppDatabase.class,
                "database-name")
                .addMigrations(MIGRATION_1_2,MIGRATION_2_3)
                .build();
        return db;
    }
}
