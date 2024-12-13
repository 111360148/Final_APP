package com.example.daily;

import android.content.Intent; // 用於啟動主畫面
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JournalActivity extends AppCompatActivity {
    private List<Journal> journalList = new ArrayList<>();
    private JournalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        RecyclerView recyclerView = findViewById(R.id.journalRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JournalAdapter(journalList);
        recyclerView.setAdapter(adapter);

        // 添加日誌條目按鈕
        findViewById(R.id.btnAddJournalEntry).setOnClickListener(v -> showAddJournalDialog());

        // 返回主畫面按鈕
        findViewById(R.id.btnBackToMain).setOnClickListener(v -> {
            Intent intent = new Intent(JournalActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // 可選，結束當前 Activity
        });
    }

    private void showAddJournalDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_journal, null);

        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etContent = view.findViewById(R.id.etContent);
        SeekBar seekBarMood = view.findViewById(R.id.seekBarMood);
        TextView tvMood = view.findViewById(R.id.tvMood);

        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMood.setText("Mood Index: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String title = etTitle.getText().toString();
                    String content = etContent.getText().toString();
                    int moodIndex = seekBarMood.getProgress();
                    // 保存日誌到列表
                    Journal journal = new Journal();
                    journal.setTitle(title);
                    journal.setContent(content);
                    journal.setMoodIndex(moodIndex);
                    journal.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                    journalList.add(journal);
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
