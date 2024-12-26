package com.example.daily;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "journal_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "journals";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_MOOD = "mood";
    private static final String COLUMN_DATE = "date";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_CONTENT + " TEXT, "
                + COLUMN_MOOD + " INTEGER, "
                + COLUMN_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void deleteJournal(Journal journal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(journal.getId())});
        db.close();
    }

    public void addJournal(Journal journal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, journal.getTitle());
        values.put(COLUMN_CONTENT, journal.getContent());
        values.put(COLUMN_MOOD, journal.getMoodIndex());
        values.put(COLUMN_DATE, journal.getDate());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public List<Journal> getAllJournals() {
        List<Journal> journalList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_DATE + " DESC");

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Journal journal = new Journal();
                        int idIndex = cursor.getColumnIndex(COLUMN_ID);
                        int titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
                        int contentIndex = cursor.getColumnIndex(COLUMN_CONTENT);
                        int moodIndex = cursor.getColumnIndex(COLUMN_MOOD);
                        int dateIndex = cursor.getColumnIndex(COLUMN_DATE);

                        if (idIndex != -1 && titleIndex != -1 && contentIndex != -1 && moodIndex != -1 && dateIndex != -1) {
                            journal.setId(cursor.getInt(idIndex));
                            journal.setTitle(cursor.getString(titleIndex));
                            journal.setContent(cursor.getString(contentIndex));
                            journal.setMoodIndex(cursor.getInt(moodIndex));
                            journal.setDate(cursor.getString(dateIndex));
                            journalList.add(journal);
                        }
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }

        db.close();
        return journalList;
    }


    public void updateJournal(Journal journal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, journal.getTitle());
        values.put(COLUMN_CONTENT, journal.getContent());
        values.put(COLUMN_MOOD, journal.getMoodIndex());
        values.put(COLUMN_DATE, journal.getDate());

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(journal.getId())});
        db.close();
    }


    public void deleteJournal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
