package com.example.perfumeshop.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.response.OrderResponse;
import com.example.perfumeshop.presentation.adapters.CheckoutAdapter;
import com.example.perfumeshop.presentation.viewmodels.CheckoutViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class CheckoutActivity extends AppCompatActivity {
    private CheckoutViewModel viewModel;
    private CheckoutAdapter checkoutAdapter;

    // Views
    private ImageView imageViewBack;
    private RecyclerView recyclerViewCheckoutItems;
    private TextView textViewOrderTotal;
    private TextInputEditText editTextCustomerName;
    private TextInputEditText editTextPhone;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextAddress;
    private RadioGroup radioGroupPayment;
    private CardView buttonPlaceOrder;
    private ProgressBar progressBarCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        setupViewModel();
        setupRecyclerView();
        setupObservers();
        setupClickListeners();
        
        // Load cart data for checkout
        viewModel.loadCheckoutData();
    }

    private void initViews() {
        imageViewBack = findViewById(R.id.imageViewBack);
        recyclerViewCheckoutItems = findViewById(R.id.recyclerViewCheckoutItems);
        textViewOrderTotal = findViewById(R.id.textViewOrderTotal);
        editTextCustomerName = findViewById(R.id.editTextFullName); // Reuse existing view
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);
        progressBarCheckout = findViewById(R.id.progressBarCheckout);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
    }

    private void setupRecyclerView() {
        checkoutAdapter = new CheckoutAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCheckoutItems.setLayoutManager(layoutManager);
        recyclerViewCheckoutItems.setAdapter(checkoutAdapter);
    }

    private void setupObservers() {
        // Observe cart items
        viewModel.getCartItemsLiveData().observe(this, cartItems -> {
            if (cartItems != null) {
                checkoutAdapter.updateItems(cartItems);
            }
        });

        // Observe total price
        viewModel.getTotalPriceLiveData().observe(this, totalPrice -> {
            if (totalPrice != null) {
                textViewOrderTotal.setText(totalPrice);
            }
        });

        // Observe loading state
        viewModel.getLoadingLiveData().observe(this, isLoading -> {
            progressBarCheckout.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            buttonPlaceOrder.setEnabled(!isLoading);
        });

        // Observe order success (for COD payments)
        viewModel.getOrderSuccessLiveData().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                showOrderSuccessScreen();
            }
        });

        // Observe order failed
        viewModel.getOrderFailedLiveData().observe(this, isFailed -> {
            if (isFailed != null && isFailed) {
                showOrderFailedScreen();
            }
        });

        // Observe payment URL (for PAYOS payments)
        viewModel.getPaymentUrlLiveData().observe(this, paymentUrl -> {
            if (paymentUrl != null && !paymentUrl.isEmpty()) {
                viewModel.openPaymentUrl(paymentUrl);
            }
        });

        // Observe order response for additional handling
        viewModel.getOrderResponseLiveData().observe(this, orderResponse -> {
            if (orderResponse != null && orderResponse.isSuccess()) {
                String message = "Order #" + orderResponse.getOrderCode() + " created successfully!";
                if (orderResponse.isCODPayment()) {
                    message += "\n" + orderResponse.getMessage();
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        // Observe error messages
        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupClickListeners() {
        imageViewBack.setOnClickListener(v -> finish());

        buttonPlaceOrder.setOnClickListener(v -> {
            if (validateInputs()) {
                placeOrder();
            }
        });
    }

    private boolean validateInputs() {
        String customerName = editTextCustomerName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if (customerName.isEmpty()) {
            editTextCustomerName.setError("Customer name is required");
            editTextCustomerName.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError("Phone number is required");
            editTextPhone.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return false;
        }

        if (address.isEmpty()) {
            editTextAddress.setError("Address is required");
            editTextAddress.requestFocus();
            return false;
        }

        return true;
    }

    private void placeOrder() {
        String customerName = editTextCustomerName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        // Get selected payment method - map to API values
        String paymentMethod = "COD"; // Default
        int selectedPaymentId = radioGroupPayment.getCheckedRadioButtonId();
        if (selectedPaymentId == R.id.radioPAYOS) {
            paymentMethod = "PAYOS";
        }

        // Create order
        viewModel.placeOrder(customerName, phone, email, address, paymentMethod);
    }

    private void showOrderSuccessScreen() {
        // Get order response data to pass to success activity
        OrderResponse orderResponse = viewModel.getOrderResponseLiveData().getValue();

        Intent intent = new Intent(this, OrderSuccessActivity.class);
        if (orderResponse != null) {
            intent.putExtra("orderCode", orderResponse.getOrderCode());
            intent.putExtra("totalAmount", orderResponse.getTotalAmount());
            intent.putExtra("paymentMethod", orderResponse.isPayOSPayment() ? "PAYOS" : "COD");
            if (orderResponse.getMessage() != null) {
                intent.putExtra("message", orderResponse.getMessage());
            }
        }

        startActivity(intent);
        finish(); // Close checkout activity
    }

    private void showOrderFailedScreen() {
        // Get error message from ViewModel
        String errorMessage = viewModel.getErrorLiveData().getValue();

        // Navigate to order failed activity with error details
        Intent intent = new Intent(this, OrderFailedActivity.class);
        intent.putExtra("errorMessage", "Order Failed");
        intent.putExtra("failureReason", errorMessage != null ? errorMessage : "We encountered an error while processing your order. Please try again.");

        startActivity(intent);
        finish(); // Close checkout activity
    }
}
