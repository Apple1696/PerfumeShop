package com.example.perfumeshop.presentation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.perfumeshop.data.local.CartManager;
import com.example.perfumeshop.data.models.entities.CartItem;

import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private CartManager cartManager;
    private MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> cartCountLiveData = new MutableLiveData<>();
    private MutableLiveData<String> totalPriceLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isEmptyLiveData = new MutableLiveData<>();

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartManager = CartManager.getInstance(application);
        loadCartData();
    }

    public void loadCartData() {
        List<CartItem> items = cartManager.getCartItems();
        cartItemsLiveData.setValue(items);
        cartCountLiveData.setValue(cartManager.getCartItemCount());
        totalPriceLiveData.setValue(cartManager.getFormattedTotalPrice());
        isEmptyLiveData.setValue(items.isEmpty());
    }

    public void updateQuantity(String perfumeId, int quantity) {
        cartManager.updateQuantity(perfumeId, quantity);
        loadCartData(); // Refresh data
    }

    public void removeItem(String perfumeId) {
        cartManager.removeFromCart(perfumeId);
        loadCartData(); // Refresh data
    }

    public void clearCart() {
        cartManager.clearCart();
        loadCartData(); // Refresh data
    }

    // Getters for LiveData
    public LiveData<List<CartItem>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }

    public LiveData<Integer> getCartCountLiveData() {
        return cartCountLiveData;
    }

    public LiveData<String> getTotalPriceLiveData() {
        return totalPriceLiveData;
    }

    public LiveData<Boolean> getIsEmptyLiveData() {
        return isEmptyLiveData;
    }
}
