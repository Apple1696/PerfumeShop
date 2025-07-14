package com.example.perfumeshop.presentation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfumeshop.ApiConfig;
import com.example.perfumeshop.R;
import com.example.perfumeshop.data.api.ApiClient;
import com.example.perfumeshop.data.api.OrderApiService;
import com.example.perfumeshop.data.api.TokenManager;
import com.example.perfumeshop.data.models.entities.Order;
import com.example.perfumeshop.data.models.response.OrderDetailResponse;
import com.example.perfumeshop.presentation.adapters.OrderItemAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private View layoutOrderDetails;
    
    // Order info views
    private TextView textViewOrderCode;
    private TextView textViewOrderDate;
    private TextView textViewOrderStatus;
    private TextView textViewPaymentStatus;
    private TextView textViewPaymentMethod;
    
    // Customer info views
    private TextView textViewCustomerName;
    private TextView textViewCustomerPhone;
    private TextView textViewCustomerEmail;
    private TextView textViewCustomerAddress;
    
    // Price info views
    private TextView textViewSubTotal;
    private TextView textViewTotalPrice;
    
    // Order items
    private RecyclerView recyclerViewOrderItems;
    private OrderItemAdapter orderItemAdapter;
    
    private OrderApiService orderApiService;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderId = getIntent().getStringExtra("order_id");
        if (orderId == null) {
            Toast.makeText(this, "Order ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupApiService();
        loadOrderDetail();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        layoutOrderDetails = findViewById(R.id.layoutOrderDetails);
        
        // Order info
        textViewOrderCode = findViewById(R.id.textViewOrderCode);
        textViewOrderDate = findViewById(R.id.textViewOrderDate);
        textViewOrderStatus = findViewById(R.id.textViewOrderStatus);
        textViewPaymentStatus = findViewById(R.id.textViewPaymentStatus);
        textViewPaymentMethod = findViewById(R.id.textViewPaymentMethod);
        
        // Customer info
        textViewCustomerName = findViewById(R.id.textViewCustomerName);
        textViewCustomerPhone = findViewById(R.id.textViewCustomerPhone);
        textViewCustomerEmail = findViewById(R.id.textViewCustomerEmail);
        textViewCustomerAddress = findViewById(R.id.textViewCustomerAddress);
        
        // Price info
        textViewSubTotal = findViewById(R.id.textViewSubTotal);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        
        // Order items
        recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Order Details");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        orderItemAdapter = new OrderItemAdapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewOrderItems.setLayoutManager(layoutManager);
        recyclerViewOrderItems.setAdapter(orderItemAdapter);
    }

    private void setupApiService() {
        orderApiService = ApiClient.getOrderApiService();
    }

    private void loadOrderDetail() {
        String token = TokenManager.getAccessToken(this);
        if (token == null) {
            Toast.makeText(this, "Authentication token not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        layoutOrderDetails.setVisibility(View.GONE);

        String authHeader = ApiConfig.BEARER + token;

        orderApiService.getOrderDetail(authHeader, orderId).enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    OrderDetailResponse orderDetailResponse = response.body();
                    if (orderDetailResponse.getOrder() != null) {
                        displayOrderDetails(orderDetailResponse.getOrder());
                        layoutOrderDetails.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "Order not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Failed to load order details", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OrderDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayOrderDetails(Order order) {
        // Order info
        textViewOrderCode.setText("Order #" + order.getOrderCode());
        textViewOrderStatus.setText(order.getOrderStatus());
        textViewPaymentStatus.setText(order.getPaymentStatus());
        textViewPaymentMethod.setText(order.getPaymentMethod());

        // Format date
        Date orderDate = order.getCreatedAtAsDate();
        if (orderDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault());
            textViewOrderDate.setText(dateFormat.format(orderDate));
        } else {
            textViewOrderDate.setText("N/A");
        }

        // Customer info
        textViewCustomerName.setText(order.getCustomerName());
        textViewCustomerPhone.setText(order.getPhone());
        textViewCustomerEmail.setText(order.getEmail());
        textViewCustomerAddress.setText(order.getAddress());

        // Price info
        textViewSubTotal.setText(String.format("%,d VND", order.getSubTotal()));
        textViewTotalPrice.setText(order.getFormattedTotalPrice());

        // Order items
        if (order.getOrderItems() != null) {
            orderItemAdapter.updateOrderItems(order.getOrderItems());
        }

        // Set status colors
        setStatusColors(order);
    }

    private void setStatusColors(Order order) {
        // Order status color
        switch (order.getOrderStatus().toLowerCase()) {
            case "pending":
                textViewOrderStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                break;
            case "completed":
            case "delivered":
                textViewOrderStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "cancelled":
                textViewOrderStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                break;
            default:
                textViewOrderStatus.setTextColor(getResources().getColor(android.R.color.black));
                break;
        }

        // Payment status color
        switch (order.getPaymentStatus().toLowerCase()) {
            case "pending":
                textViewPaymentStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                break;
            case "paid":
            case "completed":
                textViewPaymentStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "failed":
                textViewPaymentStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                break;
            default:
                textViewPaymentStatus.setTextColor(getResources().getColor(android.R.color.black));
                break;
        }
    }
}
