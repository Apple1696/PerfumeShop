package com.example.perfumeshop.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.perfumeshop.data.models.entities.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final String PREF_NAME = "cart_preferences";
    private static final String CART_ITEMS_KEY = "cart_items";
    
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private static CartManager instance;

    private CartManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context.getApplicationContext());
        }
        return instance;
    }

    public void addToCart(CartItem cartItem) {
        List<CartItem> cartItems = getCartItems();
        
        // Check if item already exists
        boolean itemExists = false;
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem existingItem = cartItems.get(i);
            if (existingItem.getPerfumeId().equals(cartItem.getPerfumeId())) {
                // Update quantity
                existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
                itemExists = true;
                break;
            }
        }
        
        // Add new item if it doesn't exist
        if (!itemExists) {
            cartItems.add(cartItem);
        }
        
        saveCartItems(cartItems);
    }

    public void removeFromCart(String perfumeId) {
        List<CartItem> cartItems = getCartItems();
        cartItems.removeIf(item -> item.getPerfumeId().equals(perfumeId));
        saveCartItems(cartItems);
    }

    public void updateQuantity(String perfumeId, int quantity) {
        List<CartItem> cartItems = getCartItems();
        for (CartItem item : cartItems) {
            if (item.getPerfumeId().equals(perfumeId)) {
                if (quantity <= 0) {
                    removeFromCart(perfumeId);
                } else {
                    item.setQuantity(quantity);
                    saveCartItems(cartItems);
                }
                break;
            }
        }
    }

    public List<CartItem> getCartItems() {
        String json = sharedPreferences.getString(CART_ITEMS_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        
        Type type = new TypeToken<List<CartItem>>(){}.getType();
        List<CartItem> items = gson.fromJson(json, type);
        return items != null ? items : new ArrayList<>();
    }

    public void clearCart() {
        sharedPreferences.edit().remove(CART_ITEMS_KEY).apply();
    }

    public int getCartItemCount() {
        List<CartItem> items = getCartItems();
        int totalCount = 0;
        for (CartItem item : items) {
            totalCount += item.getQuantity();
        }
        return totalCount;
    }

    public long getTotalPrice() {
        List<CartItem> items = getCartItems();
        long total = 0;
        for (CartItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public String getFormattedTotalPrice() {
        return String.format("%,d VND", getTotalPrice());
    }

    private void saveCartItems(List<CartItem> cartItems) {
        String json = gson.toJson(cartItems);
        sharedPreferences.edit().putString(CART_ITEMS_KEY, json).apply();
    }
}
