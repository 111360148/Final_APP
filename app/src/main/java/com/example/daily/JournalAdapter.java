package com.example.daily;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private List<Journal> journalList;

    // 构造方法
    public JournalAdapter(List<Journal> journalList) {
        this.journalList = journalList;
    }

    // 创建新的视图
    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal, parent, false);
        return new JournalViewHolder(view);
    }

    // 绑定数据到视图
    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        Journal journal = journalList.get(position);
        holder.tvDate.setText(journal.getDate());
        holder.tvTitle.setText(journal.getTitle());
        holder.tvMood.setText("Mood: " + journal.getMoodIndex());
    }

    // 获取数据的总数量
    @Override
    public int getItemCount() {
        return journalList.size();
    }

    // 更新数据源并通知适配器刷新视图
    public void updateJournalList(List<Journal> newJournalList) {
        this.journalList = newJournalList;
        notifyDataSetChanged();  // 刷新数据
    }

    // ViewHolder 类
    static class JournalViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvTitle;
        TextView tvMood;

        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMood = itemView.findViewById(R.id.tvMood);
        }
    }
}
