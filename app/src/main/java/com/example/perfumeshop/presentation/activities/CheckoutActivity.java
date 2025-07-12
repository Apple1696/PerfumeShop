package com.example.perfumeshop.presentation.activities;

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
    private TextInputEditText editTextFullName;
    private TextInputEditText editTextPhone;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextAddress;
    private TextInputEditText editTextCity;
    private TextInputEditText editTextPostalCode;
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
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextCity = findViewById(R.id.editTextCity);
        editTextPostalCode = findViewById(R.id.editTextPostalCode);
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

        // Observe order success
        viewModel.getOrderSuccessLiveData().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();
                finish();
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
        String fullName = editTextFullName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String postalCode = editTextPostalCode.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTextFullName.setError("Full name is required");
            editTextFullName.requestFocus();
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

        if (city.isEmpty()) {
            editTextCity.setError("City is required");
            editTextCity.requestFocus();
            return false;
        }

        if (postalCode.isEmpty()) {
            editTextPostalCode.setError("Postal code is required");
            editTextPostalCode.requestFocus();
            return false;
        }

        return true;
    }

    private void placeOrder() {
        String fullName = editTextFullName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String postalCode = editTextPostalCode.getText().toString().trim();
        
        // Get selected payment method
        String paymentMethod = "Cash on Delivery"; // Default
        int selectedPaymentId = radioGroupPayment.getCheckedRadioButtonId();
        if (selectedPaymentId == R.id.radioBankTransfer) {
            paymentMethod = "Bank Transfer";
        } else if (selectedPaymentId == R.id.radioCreditCard) {
            paymentMethod = "Credit/Debit Card";
        }

        // Create order
        viewModel.placeOrder(fullName, phone, email, address, city, postalCode, paymentMethod);
    }
}
