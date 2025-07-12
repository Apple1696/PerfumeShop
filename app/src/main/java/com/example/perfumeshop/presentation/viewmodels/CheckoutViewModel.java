package com.example.perfumeshop.presentation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.perfumeshop.data.local.CartManager;
import com.example.perfumeshop.data.models.entities.CartItem;

import java.util.List;

public class CheckoutViewModel extends AndroidViewModel {
    private CartManager cartManager;
    
    private MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>();
    private MutableLiveData<String> totalPriceLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> orderSuccessLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public CheckoutViewModel(@NonNull Application application) {
        super(application);
        cartManager = CartManager.getInstance(application);
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

    public void placeOrder(String fullName, String phone, String email, 
                          String address, String city, String postalCode, 
                          String paymentMethod) {
        
        loadingLiveData.setValue(true);
        
        // Simulate order processing delay
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate network delay
                
                // In a real app, you would make an API call here to place the order
                // For now, we'll just simulate success and clear the cart
                
                // Clear the cart after successful order
                cartManager.clearCart();
                
                // Post success on main thread
                loadingLiveData.postValue(false);
                orderSuccessLiveData.postValue(true);
                
            } catch (InterruptedException e) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue("Failed to place order. Please try again.");
            }
        }).start();
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
}
