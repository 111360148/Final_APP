package com.example.daily;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private final List<Journal> journalList;

    // 構造方法
    public JournalAdapter(List<Journal> journalList) {
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 創建每一條日誌的顯示視圖
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        // 取得對應位置的 Journal 資料並填充到視圖中
        Journal journal = journalList.get(position);
        holder.tvDate.setText(journal.getDate());
        holder.tvTitle.setText(journal.getTitle());
        holder.tvMood.setText("Mood: " + journal.getMoodIndex());
    }

    @Override
    public int getItemCount() {
        // 返回日誌列表的大小
        return journalList.size();
    }

    // ViewHolder 類
    static class JournalViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvTitle;
        TextView tvMood;

        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            // 初始化視圖組件
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMood = itemView.findViewById(R.id.tvMood);
        }
    }
}
