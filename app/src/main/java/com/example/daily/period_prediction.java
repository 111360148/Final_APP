package com.example.daily;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class period_prediction extends AppCompatActivity {

    private EditText edtLastPeriod, edtCycleLength, edtSearch;
    private TextView txtNextPeriod;
    private ListView listView;
    private Button btnAdd, btnUpdate, btnDelete, btnSearch, btnShowAll, btnBackToMain;

    private MyDBHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> recordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_prediction);

        edtLastPeriod = findViewById(R.id.edtLastPeriod);
        edtCycleLength = findViewById(R.id.edtCycleLength);
        edtSearch = findViewById(R.id.edtSearch);
        txtNextPeriod = findViewById(R.id.txtNextPeriod);
        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnSearch = findViewById(R.id.btnSearch);
        btnShowAll = findViewById(R.id.btnShowAll);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        dbHelper = new MyDBHelper(this);
        db = dbHelper.getWritableDatabase();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordList);
        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> addRecord());
        btnUpdate.setOnClickListener(v -> updateRecord());
        btnDelete.setOnClickListener(v -> deleteRecord());
        btnSearch.setOnClickListener(v -> searchRecord());
        btnShowAll.setOnClickListener(v -> showAllRecords());

        // 回到首頁按鈕功能
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(period_prediction.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void clearInputs() {
        edtLastPeriod.setText("");
        edtCycleLength.setText("");
        edtSearch.setText("");
        txtNextPeriod.setText("下次經期日期：");
    }

    private void addRecord() {
        String lastPeriod = edtLastPeriod.getText().toString();
        String cycleLengthStr = edtCycleLength.getText().toString();

        if (lastPeriod.isEmpty() || cycleLengthStr.isEmpty()) {
            Toast.makeText(this, "請填寫所有欄位", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar lastPeriodDate = Calendar.getInstance();
            lastPeriodDate.setTime(sdf.parse(lastPeriod));

            int cycleLength = Integer.parseInt(cycleLengthStr);
            Calendar nextPeriodDate = (Calendar) lastPeriodDate.clone();
            nextPeriodDate.add(Calendar.DAY_OF_YEAR, cycleLength);

            String nextPeriod = sdf.format(nextPeriodDate.getTime());

            db.execSQL("INSERT INTO PeriodTable (lastPeriod, nextPeriod) VALUES (?, ?)",
                    new Object[]{lastPeriod, nextPeriod});

            Toast.makeText(this, "新增成功！", Toast.LENGTH_SHORT).show();
            showAllRecords();
            clearInputs();
        } catch (Exception e) {
            Toast.makeText(this, "請輸入有效的日期格式 (yyyy-MM-dd)", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRecord() {
        String searchQuery = edtSearch.getText().toString();
        String newLastPeriod = edtLastPeriod.getText().toString();
        String cycleLengthStr = edtCycleLength.getText().toString();

        if (searchQuery.isEmpty() || newLastPeriod.isEmpty() || cycleLengthStr.isEmpty()) {
            Toast.makeText(this, "請填寫所有欄位", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar lastPeriodDate = Calendar.getInstance();
            lastPeriodDate.setTime(sdf.parse(newLastPeriod));

            int cycleLength = Integer.parseInt(cycleLengthStr);
            Calendar nextPeriodDate = (Calendar) lastPeriodDate.clone();
            nextPeriodDate.add(Calendar.DAY_OF_YEAR, cycleLength);

            String newNextPeriod = sdf.format(nextPeriodDate.getTime());

            db.execSQL("UPDATE PeriodTable SET lastPeriod = ?, nextPeriod = ? WHERE id = ?",
                    new Object[]{newLastPeriod, newNextPeriod, searchQuery});

            Toast.makeText(this, "更新成功！", Toast.LENGTH_SHORT).show();
            showAllRecords();
            clearInputs();
        } catch (Exception e) {
            Toast.makeText(this, "請輸入有效的日期格式 (yyyy-MM-dd)", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRecord() {
        String searchQuery = edtSearch.getText().toString();

        if (searchQuery.isEmpty()) {
            Toast.makeText(this, "請輸入要刪除的記錄編號", Toast.LENGTH_SHORT).show();
            return;
        }

        db.execSQL("DELETE FROM PeriodTable WHERE id = ?", new Object[]{searchQuery});
        Toast.makeText(this, "刪除成功！", Toast.LENGTH_SHORT).show();
        showAllRecords();
        clearInputs();
    }

    private void searchRecord() {
        String searchQuery = edtSearch.getText().toString();

        if (searchQuery.isEmpty()) {
            Toast.makeText(this, "請輸入要搜尋的記錄編號", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM PeriodTable WHERE id = ?", new String[]{searchQuery});
        recordList.clear();
        if (cursor.moveToFirst()) {
            String record = "ID: " + cursor.getInt(0) +
                    ", 上次經期: " + cursor.getString(1) +
                    ", 下次經期: " + cursor.getString(2);
            recordList.add(record);
        }
        cursor.close();
        adapter.notifyDataSetChanged();
        clearInputs();
    }

    private void showAllRecords() {
        Cursor cursor = db.rawQuery("SELECT * FROM PeriodTable", null);
        recordList.clear();
        while (cursor.moveToNext()) {
            String record = "ID: " + cursor.getInt(0) +
                    ", 上次經期: " + cursor.getString(1) +
                    ", 下次經期: " + cursor.getString(2);
            recordList.add(record);
        }
        cursor.close();
        adapter.notifyDataSetChanged();
        clearInputs();
    }
}