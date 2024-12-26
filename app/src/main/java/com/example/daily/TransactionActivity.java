package com.example.daily;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
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
    private SQLiteTransaction dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        dbHelper = new SQLiteTransaction(this);
        transactionList = dbHelper.getAllTransactions();

        RecyclerView recyclerView = findViewById(R.id.transactionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(transactionList);

        adapter.setOnTransactionClickListener(this::showEditTransactionDialog);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnAddTransactionEntry).setOnClickListener(v -> showAddTransactionDialog());

        findViewById(R.id.btnBackToMain).setOnClickListener(v -> {
            Intent intent = new Intent(TransactionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showAddTransactionDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_transaction, null);

        EditText etTitle = view.findViewById(R.id.etTransactionTitle);
        EditText etAmount = view.findViewById(R.id.etTransactionAmount);
        RadioGroup rgTransactionType = view.findViewById(R.id.rgTransactionType);

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String title = etTitle.getText().toString();
                    String amountStr = etAmount.getText().toString();
                    int amount = amountStr.isEmpty() ? 0 : Integer.parseInt(amountStr);

                    int selectedId = rgTransactionType.getCheckedRadioButtonId();
                    String type = null;
                    if (selectedId == R.id.rbIncome) {
                        type = "Income";
                    } else if (selectedId == R.id.rbExpense) {
                        type = "Expense";
                    }

                    if (title.isEmpty() || type == null) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    Transaction transaction = new Transaction();
                    transaction.setTitle(title);
                    transaction.setAmount(amount);
                    transaction.setDate(date);
                    transaction.setType(type);

                    long result = dbHelper.addTransaction(transaction);
                    if (result == -1) {
                        Toast.makeText(this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
                    } else {
                        transactionList.clear();
                        transactionList.addAll(dbHelper.getAllTransactions());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditTransactionDialog(Transaction transaction) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_transaction, null);

        EditText etTitle = view.findViewById(R.id.etTransactionTitle);
        EditText etAmount = view.findViewById(R.id.etTransactionAmount);
        RadioGroup rgTransactionType = view.findViewById(R.id.rgTransactionType);

        etTitle.setText(transaction.getTitle());

        etAmount.setText(String.valueOf(transaction.getAmount()));
        if ("Income".equals(transaction.getType())) {
            rgTransactionType.check(R.id.rbIncome);
        } else if ("Expense".equals(transaction.getType())) {
            rgTransactionType.check(R.id.rbExpense);
        }

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Update", (dialog, which) -> {
                    String title = etTitle.getText().toString();
                    String amountStr = etAmount.getText().toString();
                    // 將amount轉換為整數
                    int amount = amountStr.isEmpty() ? 0 : Integer.parseInt(amountStr);

                    int selectedId = rgTransactionType.getCheckedRadioButtonId();
                    String type = null;
                    if (selectedId == R.id.rbIncome) {
                        type = "Income";
                    } else if (selectedId == R.id.rbExpense) {
                        type = "Expense";
                    }

                    transaction.setTitle(title);
                    transaction.setAmount(amount);
                    transaction.setType(type);

                    int result = dbHelper.updateTransaction(transaction);
                    if (result == -1) {
                        Toast.makeText(this, "Failed to update transaction", Toast.LENGTH_SHORT).show();
                    } else {
                        transactionList.clear();
                        transactionList.addAll(dbHelper.getAllTransactions());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Transaction updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Delete", (dialog, which) -> {
                    dbHelper.deleteTransaction(transaction);
                    transactionList.clear();
                    transactionList.addAll(dbHelper.getAllTransactions());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Transaction deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
