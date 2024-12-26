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
    private OnJournalClickListener listener;

    public JournalAdapter(List<Journal> journalList) {
        this.journalList = journalList;
    }

    public void setOnJournalClickListener(OnJournalClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        Journal journal = journalList.get(position);
        holder.tvDate.setText(journal.getDate());
        holder.tvTitle.setText(journal.getTitle());
        holder.tvMood.setText("Mood: " + journal.getMoodIndex());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJournalClick(journal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public void updateJournalList(List<Journal> newJournalList) {
        this.journalList = newJournalList;
        notifyDataSetChanged();
    }

    public interface OnJournalClickListener {
        void onJournalClick(Journal journal);
    }

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
