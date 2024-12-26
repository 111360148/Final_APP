package com.example.daily;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;
    private OnTransactionClickListener onTransactionClickListener;

    // 定義點擊事件接口
    public interface OnTransactionClickListener {
        void onTransactionClick(Transaction transaction);
    }

    // 設置點擊事件監聽器
    public void setOnTransactionClickListener(OnTransactionClickListener listener) {
        this.onTransactionClickListener = listener;
    }

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvAmount;
        private TextView tvDate;
        private TextView tvType;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTransactionTitle);
            tvAmount = itemView.findViewById(R.id.tvTransactionAmount);
            tvDate = itemView.findViewById(R.id.tvTransactionDate);
            tvType = itemView.findViewById(R.id.tvTransactionType);

            itemView.setOnClickListener(v -> {
                if (onTransactionClickListener != null) {
                    onTransactionClickListener.onTransactionClick(transactionList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Transaction transaction) {
            tvTitle.setText(transaction.getTitle());
            tvAmount.setText(String.valueOf(transaction.getAmount()));
            tvDate.setText(transaction.getDate());
            tvType.setText(transaction.getType());
        }
    }
}
