package com.example.perfumeshop.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.perfumeshop.R;

public class OrderFailedActivity extends AppCompatActivity {

    private TextView textViewErrorMessage;
    private TextView textViewOrderCode;
    private TextView textViewFailureReason;
    private Button buttonRetryOrder;
    private Button buttonBackToHome;
    private ImageView imageViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_failed);

        initViews();
        setupData();
        setupClickListeners();
    }

    private void initViews() {
        textViewErrorMessage = findViewById(R.id.textViewErrorMessage);
        textViewOrderCode = findViewById(R.id.textViewOrderCode);
        textViewFailureReason = findViewById(R.id.textViewFailureReason);
        buttonRetryOrder = findViewById(R.id.buttonRetryOrder);
        buttonBackToHome = findViewById(R.id.buttonBackToHome);
        imageViewError = findViewById(R.id.imageViewError);
    }

    private void setupData() {
        Intent intent = getIntent();
        if (intent != null) {
            String errorMessage = intent.getStringExtra("errorMessage");
            String orderCode = intent.getStringExtra("orderCode");
            String failureReason = intent.getStringExtra("failureReason");

            if (errorMessage != null && !errorMessage.isEmpty()) {
                textViewErrorMessage.setText(errorMessage);
            } else {
                textViewErrorMessage.setText("Order Failed");
            }

            if (orderCode != null && !orderCode.isEmpty()) {
                textViewOrderCode.setText("Order #" + orderCode);
                textViewOrderCode.setVisibility(android.view.View.VISIBLE);
            } else {
                textViewOrderCode.setVisibility(android.view.View.GONE);
            }

            if (failureReason != null && !failureReason.isEmpty()) {
                textViewFailureReason.setText(failureReason);
            } else {
                textViewFailureReason.setText("We encountered an error while processing your order. Please try again or contact support if the problem persists.");
            }
        }
    }

    private void setupClickListeners() {
        buttonRetryOrder.setOnClickListener(v -> {
            // Navigate back to checkout to retry the order
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        buttonBackToHome.setOnClickListener(v -> {
            // Navigate back to main activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Override back press to go to home instead of previous activity
        buttonBackToHome.performClick();
    }
}
