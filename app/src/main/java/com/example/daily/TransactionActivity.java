package com.example.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionActivity extends AppCompatActivity {
    private List<Transaction> transactionList = new ArrayList<>();
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        RecyclerView recyclerView = findViewById(R.id.transactionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnAddTransactionEntry).setOnClickListener(v -> showAddTransactionDialog());
    }

    private void showAddTransactionDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_transaction, null);

        EditText etTitle = view.findViewById(R.id.etTransactionTitle);
        EditText etAmount = view.findViewById(R.id.etTransactionAmount);

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String title = etTitle.getText().toString();
                    String amountStr = etAmount.getText().toString();
                    double amount = amountStr.isEmpty() ? 0 : Double.parseDouble(amountStr);
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    // 創建 Transaction 對象並添加到列表
                    Transaction transaction = new Transaction();
                    transaction.setTitle(title);
                    transaction.setAmount(amount);
                    transaction.setDate(date);

                    transactionList.add(transaction);
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
