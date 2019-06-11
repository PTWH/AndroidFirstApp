package com.example.myfirstapp.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TokenData {
    @PrimaryKey
    @ColumnInfo(name = "TOKEN")
    @NonNull
    public String token;
    @ColumnInfo(name = "LANG")
    public String lang;
}
