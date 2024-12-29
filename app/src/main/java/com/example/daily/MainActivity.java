package com.example.daily;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnAddJournal).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, JournalActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddTransaction).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddRecord).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, period_prediction.class);
            startActivity(intent);
        });

        // 新增額外功能 (使用 API)
        findViewById(R.id.btn_search).setOnClickListener(v -> {
            String URL = "https://vbs.sports.taipei/opendata/sports_tms2.json";

            Request request = new Request.Builder().url(URL).build();

            OkHttpClient OkHttpClient = UnsafeOkHttpClient.getUnsafeOKHttpClient();
            OkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        if (response.body() == null) return;
                        String json = response.body().string();

                        Type listType = new TypeToken<List<Data>>() {}.getType();
                        List<Data> dataList = new Gson().fromJson(json, listType);

                        final String[] items = new String[dataList.size()];

                        for (int i = 0; i < items.length; i++) {
                            Data data = dataList.get(i);
                            items[i] = "\n區域：" + data.Area +
                                    "\n名稱：" + data.Name +
                                    "\n運動類型：" + data.SportType +
                                    "\n地址：" + data.Address +
                                    "\n營業時間：" + data.startTime + " - " + data.endTime +
                                    "\n聯絡電話：" + data.LocalCallService;
                        }

                        runOnUiThread(() -> {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("臺北市體育局場地資訊")
                                    .setItems(items, null)
                                    .show();
                        });
                    } else {
                        Log.e("伺服器錯誤", response.code() + " " + response.message());
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("查詢失敗", e.getMessage());
                }
            });
        });
    }
}
