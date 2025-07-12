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
import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.entities.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private NumberFormat currencyFormat;

    public CheckoutAdapter(Context context) {
        this.context = context;
        this.cartItems = new ArrayList<>();
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
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

        holder.textViewPerfumeName.setText(item.getPerfumeName());
        holder.textViewBrand.setText(item.getBrandName());
        holder.textViewQuantity.setText("Qty: " + item.getQuantity());
        holder.textViewUnitPrice.setText(String.format(Locale.getDefault(), "%,d VND", item.getPrice()));
        holder.textViewTotalPrice.setText(String.format(Locale.getDefault(), "%,d VND", item.getPrice() * item.getQuantity()));

        // Load perfume image
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.placeholder_perfume)
                    .error(R.drawable.placeholder_perfume)
                    .into(holder.imageViewPerfume);
        } else {
            holder.imageViewPerfume.setImageResource(R.drawable.placeholder_perfume);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateItems(List<CartItem> newItems) {
        this.cartItems.clear();
        if (newItems != null) {
            this.cartItems.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPerfume;
        TextView textViewPerfumeName;
        TextView textViewBrand;
        TextView textViewQuantity;
        TextView textViewUnitPrice;
        TextView textViewTotalPrice;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPerfume = itemView.findViewById(R.id.imageViewPerfume);
            textViewPerfumeName = itemView.findViewById(R.id.textViewPerfumeName);
            textViewBrand = itemView.findViewById(R.id.textViewBrand);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewUnitPrice = itemView.findViewById(R.id.textViewUnitPrice);
            textViewTotalPrice = itemView.findViewById(R.id.textViewTotalPrice);
        }
    }
}
