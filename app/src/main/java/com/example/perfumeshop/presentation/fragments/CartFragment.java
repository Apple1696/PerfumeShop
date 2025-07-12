package com.example.perfumeshop.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.entities.CartItem;
import com.example.perfumeshop.presentation.activities.CheckoutActivity;
import com.example.perfumeshop.presentation.activities.PerfumeDetailsActivity;
import com.example.perfumeshop.presentation.adapters.CartAdapter;
import com.example.perfumeshop.presentation.viewmodels.CartViewModel;

public class CartFragment extends Fragment implements CartAdapter.OnCartItemActionListener {
    private CartViewModel viewModel;
    private CartAdapter cartAdapter;

    // Views
    private RecyclerView recyclerViewCartItems;
    private LinearLayout layoutEmptyCart;
    private CardView cardViewSummary;
    private TextView textViewCartCount;
    private TextView textViewTotalPrice;
    private CardView buttonClearCart;
    private CardView buttonCheckout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        
        initViews(view);
        setupViewModel();
        setupRecyclerView();
        setupObservers();
        setupClickListeners();
        
        return view;
    }

    private void initViews(View view) {
        recyclerViewCartItems = view.findViewById(R.id.recyclerViewCartItems);
        layoutEmptyCart = view.findViewById(R.id.layoutEmptyCart);
        cardViewSummary = view.findViewById(R.id.cardViewSummary);
        textViewCartCount = view.findViewById(R.id.textViewCartCount);
        textViewTotalPrice = view.findViewById(R.id.textViewTotalPrice);
        buttonClearCart = view.findViewById(R.id.buttonClearCart);
        buttonCheckout = view.findViewById(R.id.buttonCheckout);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(getContext());
        cartAdapter.setOnCartItemActionListener(this);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewCartItems.setLayoutManager(layoutManager);
        recyclerViewCartItems.setAdapter(cartAdapter);
    }

    private void setupObservers() {
        // Observe cart items
        viewModel.getCartItemsLiveData().observe(getViewLifecycleOwner(), cartItems -> {
            if (cartItems != null) {
                cartAdapter.updateCartItems(cartItems);
            }
        });

        // Observe cart count
        viewModel.getCartCountLiveData().observe(getViewLifecycleOwner(), count -> {
            String countText = count + (count == 1 ? " item" : " items");
            textViewCartCount.setText(countText);
        });

        // Observe total price
        viewModel.getTotalPriceLiveData().observe(getViewLifecycleOwner(), totalPrice -> {
            textViewTotalPrice.setText(totalPrice);
        });

        // Observe empty state
        viewModel.getIsEmptyLiveData().observe(getViewLifecycleOwner(), isEmpty -> {
            if (isEmpty != null) {
                layoutEmptyCart.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                recyclerViewCartItems.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                cardViewSummary.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void setupClickListeners() {
        buttonClearCart.setOnClickListener(v -> {
            viewModel.clearCart();
            Toast.makeText(getContext(), "Cart cleared", Toast.LENGTH_SHORT).show();
        });        buttonCheckout.setOnClickListener(v -> {
            // Navigate to checkout activity
            Intent intent = new Intent(getContext(), CheckoutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh cart data when fragment becomes visible
        viewModel.loadCartData();
    }

    // CartAdapter.OnCartItemActionListener implementation
    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        viewModel.updateQuantity(item.getPerfumeId(), newQuantity);
        Toast.makeText(getContext(), "Quantity updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemRemoved(CartItem item) {
        viewModel.removeItem(item.getPerfumeId());
        Toast.makeText(getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClicked(CartItem item) {
        // Navigate to perfume details
        Intent intent = new Intent(getContext(), PerfumeDetailsActivity.class);
        intent.putExtra("perfume_id", item.getPerfumeId());
        startActivity(intent);
    }
}