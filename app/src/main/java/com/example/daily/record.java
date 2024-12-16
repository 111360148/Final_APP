package com.example.daily;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class record extends AppCompatActivity {

    private EditText edtLastPeriod;
    private EditText edtCycleLength;
    private TextView txtNextPeriod;
    private Button btnCalculate;
    private Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        edtLastPeriod = findViewById(R.id.edtLastPeriod);
        edtCycleLength = findViewById(R.id.edtCycleLength);
        txtNextPeriod = findViewById(R.id.txtNextPeriod);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        btnCalculate.setOnClickListener(v -> calculateNextPeriod());
        // 返回主畫面按鈕
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(record.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void calculateNextPeriod() {
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
            txtNextPeriod.setText("下次經期日期: " + nextPeriod);
        } catch (Exception e) {
            Toast.makeText(this, "請輸入有效的日期格式 (yyyy-MM-dd)", Toast.LENGTH_SHORT).show();
        }
    }
}
