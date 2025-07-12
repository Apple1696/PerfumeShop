package com.example.perfumeshop.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.response.OrderResponse;

public class OrderSuccessActivity extends AppCompatActivity {

    private TextView textViewOrderNumber;
    private TextView textViewTotalAmount;
    private TextView textViewOrderMessage;
    private Button buttonContinueShopping;
    private ImageView imageViewSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        initViews();
        setupData();
        setupClickListeners();
    }

    private void initViews() {
        textViewOrderNumber = findViewById(R.id.textViewOrderNumber);
        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        textViewOrderMessage = findViewById(R.id.textViewOrderMessage);
        buttonContinueShopping = findViewById(R.id.buttonContinueShopping);
        imageViewSuccess = findViewById(R.id.imageViewSuccess);
    }

    private void setupData() {
        Intent intent = getIntent();
        if (intent != null) {
            long orderCode = intent.getLongExtra("orderCode", 0);
            long totalAmount = intent.getLongExtra("totalAmount", 0);
            String paymentMethod = intent.getStringExtra("paymentMethod");
            String message = intent.getStringExtra("message");

            if (orderCode > 0) {
                textViewOrderNumber.setText("Order #" + orderCode);
            }

            if (totalAmount > 0) {
                textViewTotalAmount.setText(String.format("Total: %,d VND", totalAmount));
            }

            if ("COD".equals(paymentMethod) && message != null) {
                textViewOrderMessage.setText(message);
            } else if ("PAYOS".equals(paymentMethod)) {
                textViewOrderMessage.setText("Your payment has been processed. You will receive a confirmation email shortly.");
            } else {
                textViewOrderMessage.setText("Thank you for your order!");
            }
        }
    }

    private void setupClickListeners() {
        buttonContinueShopping.setOnClickListener(v -> {
            // Navigate back to main activity and clear the task stack
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Override back press to prevent going back to checkout
        buttonContinueShopping.performClick();
    }
}
