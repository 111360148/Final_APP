package com.example.daily;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


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

    }


}
