package com.example.perfumeshop.presentation.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfumeshop.ApiConfig;
import com.example.perfumeshop.R;
import com.example.perfumeshop.data.api.ApiClient;
import com.example.perfumeshop.data.api.OrderApiService;
import com.example.perfumeshop.data.api.TokenManager;
import com.example.perfumeshop.data.models.entities.Order;
import com.example.perfumeshop.data.models.response.OrderHistoryResponse;
import com.example.perfumeshop.presentation.activities.OrderDetailActivity;
import com.example.perfumeshop.presentation.adapters.OrderHistoryAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends Fragment implements OrderHistoryAdapter.OnOrderClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerViewOrders;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty; // Changed from TextView to LinearLayout
    private OrderHistoryAdapter orderAdapter;
    private OrderApiService orderApiService;
    private List<Order> orderList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        initViews(view);
        setupToolbar();
        setupRecyclerView();
        setupApiService();
        loadOrderHistory();

        return view;
    }

    private void initViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        progressBar = view.findViewById(R.id.progressBar);
        layoutEmpty = view.findViewById(R.id.textViewEmpty); // Changed variable name and type
    }

    private void setupToolbar() {
        toolbar.setTitle("Order History");
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderHistoryAdapter(getContext(), orderList);
        orderAdapter.setOnOrderClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewOrders.setLayoutManager(layoutManager);
        recyclerViewOrders.setAdapter(orderAdapter);
    }

    private void setupApiService() {
        orderApiService = ApiClient.getOrderApiService();
    }

    private void loadOrderHistory() {
        if (getContext() == null) return;

        String token = TokenManager.getAccessToken(requireContext());
        if (token == null || token.isEmpty()) {
            Toast.makeText(getContext(), "Authentication token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
        recyclerViewOrders.setVisibility(View.GONE);

        String authHeader = ApiConfig.BEARER + token;

        orderApiService.getUserOrders(authHeader).enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (getContext() == null) return; // Fragment might be detached

                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    OrderHistoryResponse orderResponse = response.body();
                    if (orderResponse.getOrders() != null && !orderResponse.getOrders().isEmpty()) {
                        orderList.clear();

                        // Sort orders by createdAt date in descending order (newest first)
                        List<Order> sortedOrders = new ArrayList<>(orderResponse.getOrders());
                        Collections.sort(sortedOrders, (order1, order2) -> {
                            Date date1 = order1.getCreatedAtAsDate();
                            Date date2 = order2.getCreatedAtAsDate();

                            // Handle null dates
                            if (date1 == null && date2 == null) return 0;
                            if (date1 == null) return 1;
                            if (date2 == null) return -1;

                            // Sort in descending order (newest first)
                            return date2.compareTo(date1);
                        });

                        orderList.addAll(sortedOrders);
                        if (orderAdapter != null) {
                            orderAdapter.notifyDataSetChanged();
                        }
                        recyclerViewOrders.setVisibility(View.VISIBLE);
                        layoutEmpty.setVisibility(View.GONE);
                    } else {
                        showEmptyState();
                    }
                } else {
                    String errorMessage = "Failed to load order history";
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            if (!errorBody.isEmpty()) {
                                errorMessage += ": " + errorBody;
                            }
                        } catch (Exception e) {
                            errorMessage += ": " + response.message();
                        }
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    showEmptyState();
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                // Failure handling remains unchanged
            }
        });
    }

    private void showEmptyState() {
        recyclerViewOrders.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE); // Updated variable name
    }

    @Override
    public void onOrderClick(Order order) {
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra("order_id", order.get_id());
        startActivity(intent);
    }
}
