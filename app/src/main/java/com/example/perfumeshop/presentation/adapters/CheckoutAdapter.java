package com.example.perfumeshop.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.entities.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {
    private Context context;
    private List<CartItem> cartItems;

    public CheckoutAdapter(Context context) {
        this.context = context;
        this.cartItems = new ArrayList<>();
    }

    public void updateItems(List<CartItem> newItems) {
        this.cartItems.clear();
        this.cartItems.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CheckoutViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPerfume;
        private TextView textViewPerfumeName;
        private TextView textViewBrand;
        private TextView textViewQuantity;
        private TextView textViewUnitPrice;
        private TextView textViewTotalPrice;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            
            imageViewPerfume = itemView.findViewById(R.id.imageViewPerfume);
            textViewPerfumeName = itemView.findViewById(R.id.textViewPerfumeName);
            textViewBrand = itemView.findViewById(R.id.textViewBrand);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewUnitPrice = itemView.findViewById(R.id.textViewUnitPrice);
            textViewTotalPrice = itemView.findViewById(R.id.textViewTotalPrice);
        }

        public void bind(CartItem item) {
            // Set basic information
            textViewPerfumeName.setText(item.getPerfumeName());
            textViewBrand.setText(item.getBrandName());
            textViewQuantity.setText("Qty: " + item.getQuantity());
            textViewUnitPrice.setText(item.getFormattedPrice());
            textViewTotalPrice.setText(item.getFormattedTotalPrice());

            // Load image
            loadImage(item.getImageUrl());
        }

        private void loadImage(String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(context)
                        .load(imageUrl)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.placeholder_perfume)
                                .error(R.drawable.placeholder_perfume))
                        .into(imageViewPerfume);
            } else {
                imageViewPerfume.setImageResource(R.drawable.placeholder_perfume);
            }
        }
    }
}
