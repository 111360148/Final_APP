package com.example.daily;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String name = "mDatabase.db";
    private static final int version = 1;

    public MyDBHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE PeriodTable (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "lastPeriod TEXT NOT NULL, " +
                "nextPeriod TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PeriodTable");
        onCreate(db);
    }
}
