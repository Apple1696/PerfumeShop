package com.example.perfumeshop.presentation.viewmodels;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.perfumeshop.data.api.ApiClient;
import com.example.perfumeshop.data.api.ApiService;
import com.example.perfumeshop.data.local.CartManager;
import com.example.perfumeshop.data.models.entities.CartItem;
import com.example.perfumeshop.data.models.request.OrderRequest;
import com.example.perfumeshop.data.models.response.OrderResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutViewModel extends AndroidViewModel {
    private CartManager cartManager;
    private ApiService apiService;

    private MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>();
    private MutableLiveData<String> totalPriceLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> orderSuccessLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> orderFailedLiveData = new MutableLiveData<>();
    private MutableLiveData<String> paymentUrlLiveData = new MutableLiveData<>();
    private MutableLiveData<OrderResponse> orderResponseLiveData = new MutableLiveData<>();

    public CheckoutViewModel(@NonNull Application application) {
        super(application);
        cartManager = CartManager.getInstance(application);
        apiService = ApiClient.getApiService();
    }

    public void loadCheckoutData() {
        List<CartItem> cartItems = cartManager.getCartItems();
        
        if (cartItems.isEmpty()) {
            errorLiveData.setValue("No items in cart");
            return;
        }
        
        cartItemsLiveData.setValue(cartItems);
        totalPriceLiveData.setValue(cartManager.getFormattedTotalPrice());
    }

    public void placeOrder(String customerName, String phone, String email,
                          String address, String paymentMethod) {

        loadingLiveData.setValue(true);
        
        List<CartItem> cartItems = cartManager.getCartItems();
        if (cartItems.isEmpty()) {
            loadingLiveData.setValue(false);
            errorLiveData.setValue("No items in cart");
            return;
        }

        // Convert cart items to order items
        List<OrderRequest.OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            orderItems.add(new OrderRequest.OrderItem(cartItem.getPerfumeId(), cartItem.getQuantity()));
        }

        // Create order request
        OrderRequest orderRequest = new OrderRequest(
            address,
            phone,
            email,
            customerName,
            paymentMethod,
            orderItems
        );

        // Make API call
        Call<OrderResponse> call = apiService.createOrder(orderRequest);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                loadingLiveData.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    OrderResponse orderResponse = response.body();

                    if (orderResponse.isSuccess()) {
                        // Clear the cart after successful order
                        cartManager.clearCart();

                        // Store order response for further processing
                        orderResponseLiveData.setValue(orderResponse);

                        if (orderResponse.isPayOSPayment()) {
                            // For PAYOS payment, set the payment URL to open in browser
                            paymentUrlLiveData.setValue(orderResponse.getPaymentUrl());
                        } else {
                            // For COD payment, show success screen
                            orderSuccessLiveData.setValue(true);
                        }
                    } else {
                        // Order failed at API level
                        orderFailedLiveData.setValue(true);
                        errorLiveData.setValue("Failed to create order");
                    }
                } else {
                    // HTTP error or response parsing failed
                    orderFailedLiveData.setValue(true);
                    if (response.code() == 400) {
                        errorLiveData.setValue("Invalid order data. Please check your information.");
                    } else if (response.code() == 500) {
                        errorLiveData.setValue("Server error. Please try again later.");
                    } else {
                        errorLiveData.setValue("Network error. Please try again.");
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                loadingLiveData.setValue(false);
                orderFailedLiveData.setValue(true);
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void openPaymentUrl(String paymentUrl) {
        if (paymentUrl != null && !paymentUrl.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(paymentUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(intent);
        }
    }

    // LiveData getters
    public LiveData<List<CartItem>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }

    public LiveData<String> getTotalPriceLiveData() {
        return totalPriceLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public LiveData<Boolean> getOrderSuccessLiveData() {
        return orderSuccessLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getOrderFailedLiveData() {
        return orderFailedLiveData;
    }

    public LiveData<String> getPaymentUrlLiveData() {
        return paymentUrlLiveData;
    }

    public LiveData<OrderResponse> getOrderResponseLiveData() {
        return orderResponseLiveData;
    }
}
