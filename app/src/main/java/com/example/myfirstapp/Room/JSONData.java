package com.example.myfirstapp.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class JSONData {
    @PrimaryKey
    @ColumnInfo(name = "ID")
    @NonNull
    public String uniqueid;
    @ColumnInfo(name = "JS_DATA")
    public String JSONStr;
    @ColumnInfo(name = "JS_URL")
    public String JSONUrl;

}
