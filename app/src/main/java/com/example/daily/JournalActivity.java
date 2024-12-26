package com.example.daily;

import android.content.Intent;
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
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        dbHelper = new SQLiteHelper(this);
        RecyclerView recyclerView = findViewById(R.id.journalRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JournalAdapter(journalList);
        recyclerView.setAdapter(adapter);

        adapter.setOnJournalClickListener(this::showEditJournalDialog);

        loadJournalsFromDatabase();

        findViewById(R.id.btnAddJournalEntry).setOnClickListener(v -> showAddJournalDialog());

        findViewById(R.id.btnBackToMain).setOnClickListener(v -> {
            Intent intent = new Intent(JournalActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadJournalsFromDatabase() {
        journalList.clear();
        journalList.addAll(dbHelper.getAllJournals());
        adapter.notifyDataSetChanged();
        displayMoodAverage();
    }

    private void displayMoodAverage() {
        List<Journal> last7Journals = getLast7Journals();

        if (last7Journals.size() > 0) {
            int totalMoodIndex = 0;
            for (Journal journal : last7Journals) {
                totalMoodIndex += journal.getMoodIndex();
            }
            float averageMood = totalMoodIndex / (float) last7Journals.size();
            TextView tvAverageMood = findViewById(R.id.tvAverageMood);
            tvAverageMood.setText("Average Mood Index: " + averageMood);
        }
    }

    private List<Journal> getLast7Journals() {
        List<Journal> last7 = new ArrayList<>();
        int size = journalList.size();
        for (int i = Math.max(0, size - 7); i < size; i++) {
            last7.add(journalList.get(i));
        }
        return last7;
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
                    Journal journal = new Journal();
                    journal.setTitle(title);
                    journal.setContent(content);
                    journal.setMoodIndex(moodIndex);
                    journal.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                    dbHelper.addJournal(journal);
                    loadJournalsFromDatabase();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditJournalDialog(Journal journal) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_journal, null);

        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etContent = view.findViewById(R.id.etContent);
        SeekBar seekBarMood = view.findViewById(R.id.seekBarMood);
        TextView tvMood = view.findViewById(R.id.tvMood);

        etTitle.setText(journal.getTitle());
        etContent.setText(journal.getContent());
        seekBarMood.setProgress(journal.getMoodIndex());
        tvMood.setText("Mood Index: " + journal.getMoodIndex());

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
                .setPositiveButton("Update", (dialog, which) -> {
                    String title = etTitle.getText().toString();
                    String content = etContent.getText().toString();
                    int moodIndex = seekBarMood.getProgress();
                    journal.setTitle(title);
                    journal.setContent(content);
                    journal.setMoodIndex(moodIndex);
                    dbHelper.updateJournal(journal);
                    loadJournalsFromDatabase();
                })
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Delete", (dialog, which) -> {
                    dbHelper.deleteJournal(journal);
                    loadJournalsFromDatabase();
                })
                .show();
    }
}
