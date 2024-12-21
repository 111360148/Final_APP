package com.example.daily;

import android.content.Intent;
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
    private SQLiteTransaction dbHelper;  // 使用 SQLiteTransaction 类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // 初始化 SQLiteTransaction 对象
        dbHelper = new SQLiteTransaction(this);

        // 从数据库加载交易记录
        transactionList = dbHelper.getAllTransactions();

        // 设置 RecyclerView
        RecyclerView recyclerView = findViewById(R.id.transactionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(adapter);

        // 添加交易记录按钮
        findViewById(R.id.btnAddTransactionEntry).setOnClickListener(v -> showAddTransactionDialog());

        // 返回主界面按钮
        findViewById(R.id.btnBackToMain).setOnClickListener(v -> {
            Intent intent = new Intent(TransactionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // 显示添加交易记录对话框
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

                    // 创建 Transaction 对象
                    Transaction transaction = new Transaction();
                    transaction.setTitle(title);
                    transaction.setAmount(amount);
                    transaction.setDate(date);

                    // 将交易记录添加到数据库
                    dbHelper.addTransaction(transaction);

                    // 更新交易记录列表并刷新 RecyclerView
                    transactionList.clear();
                    transactionList.addAll(dbHelper.getAllTransactions());
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
