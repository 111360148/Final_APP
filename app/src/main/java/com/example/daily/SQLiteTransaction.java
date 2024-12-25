package com.example.daily;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteTransaction extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "transactions.db";
    private static final int DATABASE_VERSION = 2; // 更新版本號

    private static final String TABLE_NAME = "transactions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type"; // 新增欄位

    public SQLiteTransaction(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TYPE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_TYPE + " TEXT");
        }
    }

    public long addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, transaction.getTitle());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_DATE, transaction.getDate());
        values.put(COLUMN_TYPE, transaction.getType()); // 儲存類型

        long rowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return rowId;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_AMOUNT, COLUMN_DATE, COLUMN_TYPE},
                null, null, null, null, COLUMN_DATE + " DESC");

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Transaction transaction = new Transaction();
                        transaction.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                        transaction.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                        transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
                        transaction.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                        transaction.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                        transactionList.add(transaction);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        db.close();
        return transactionList;
    }
}
