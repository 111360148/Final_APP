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
    private static final int DATABASE_VERSION = 1;

    // 表名
    private static final String TABLE_NAME = "transactions";

    // 列名
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";

    public SQLiteTransaction(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 创建数据库表
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_DATE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    // 更新数据库（如果数据库版本发生更改时调用）
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 插入交易记录
    public void addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, transaction.getTitle());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_DATE, transaction.getDate());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // 获取所有交易记录
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_AMOUNT, COLUMN_DATE},
                null, null, null, null, COLUMN_DATE + " DESC");

        if (cursor != null) {
            try {
                // 检查是否有数据
                if (cursor.moveToFirst()) {
                    do {
                        // 安全获取列索引和列数据
                        int titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
                        int amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
                        int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
                        int idIndex = cursor.getColumnIndex(COLUMN_ID);

                        // 确保列索引有效
                        if (titleIndex != -1 && amountIndex != -1 && dateIndex != -1 && idIndex != -1) {
                            // 创建 Transaction 对象并设置属性
                            Transaction transaction = new Transaction();
                            transaction.setTitle(cursor.getString(titleIndex));
                            transaction.setAmount(cursor.getDouble(amountIndex));
                            transaction.setDate(cursor.getString(dateIndex));
                            transaction.setId(cursor.getInt(idIndex));

                            // 将交易记录添加到列表
                            transactionList.add(transaction);
                        }
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace(); // 捕获异常并打印
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close(); // 确保最终关闭 cursor
                }
            }
        }

        db.close();
        return transactionList;
    }
}
