package com.example.perfumeshop.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.entities.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemActionListener listener;

    public interface OnCartItemActionListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemRemoved(CartItem item);
        void onItemClicked(CartItem item);
    }

    public CartAdapter(Context context) {
        this.context = context;
        this.cartItems = new ArrayList<>();
    }

    public void setOnCartItemActionListener(OnCartItemActionListener listener) {
        this.listener = listener;
    }

    public void updateCartItems(List<CartItem> newItems) {
        this.cartItems.clear();
        this.cartItems.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPerfume;
        private ProgressBar progressBarImage;
        private TextView textViewPerfumeName;
        private TextView textViewBrand;
        private TextView textViewVolume;
        private TextView textViewTargetAudience;
        private TextView textViewPrice;
        private TextView textViewQuantity;
        private ImageView imageViewRemove;
        private CardView buttonDecrease;
        private CardView buttonIncrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            
            imageViewPerfume = itemView.findViewById(R.id.imageViewPerfume);
            progressBarImage = itemView.findViewById(R.id.progressBarImage);
            textViewPerfumeName = itemView.findViewById(R.id.textViewPerfumeName);
            textViewBrand = itemView.findViewById(R.id.textViewBrand);
            textViewVolume = itemView.findViewById(R.id.textViewVolume);
            textViewTargetAudience = itemView.findViewById(R.id.textViewTargetAudience);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            imageViewRemove = itemView.findViewById(R.id.imageViewRemove);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
        }        public void bind(CartItem item) {
            // Set basic information
            textViewPerfumeName.setText(item.getPerfumeName());
            textViewBrand.setText(item.getBrandName());
            textViewVolume.setText(item.getVolume() + "ml");
            textViewTargetAudience.setText(item.getTargetAudience());
            
            // Set background color for target audience
            setTargetAudienceBackground(textViewTargetAudience, item.getTargetAudience());
            
            textViewPrice.setText(item.getFormattedTotalPrice());
            textViewQuantity.setText(String.valueOf(item.getQuantity()));

            // Load image
            loadImage(item.getImageUrl());

            // Set click listeners
            setupClickListeners(item);
        }

        private void loadImage(String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                progressBarImage.setVisibility(View.VISIBLE);
                
                Glide.with(context)
                        .load(imageUrl)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.placeholder_perfume)
                                .error(R.drawable.placeholder_perfume))
                        .into(imageViewPerfume);
                
                progressBarImage.setVisibility(View.GONE);
            } else {
                imageViewPerfume.setImageResource(R.drawable.placeholder_perfume);
            }
        }

        private void setupClickListeners(CartItem item) {
            // Item click
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClicked(item);
                }
            });

            // Remove item
            imageViewRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemRemoved(item);
                }
            });

            // Decrease quantity
            buttonDecrease.setOnClickListener(v -> {
                int currentQuantity = item.getQuantity();
                if (currentQuantity > 1 && listener != null) {
                    listener.onQuantityChanged(item, currentQuantity - 1);
                }
            });

            // Increase quantity
            buttonIncrease.setOnClickListener(v -> {
                int currentQuantity = item.getQuantity();
                if (listener != null) {
                    listener.onQuantityChanged(item, currentQuantity + 1);
                }
            });
        }
        
        private void setTargetAudienceBackground(TextView textView, String targetAudience) {
            int backgroundRes;
            if (targetAudience != null) {
                switch (targetAudience.toLowerCase()) {
                    case "male":
                        backgroundRes = R.color.male_bg;
                        break;
                    case "female":
                        backgroundRes = R.color.female_bg;
                        break;
                    case "unisex":
                        backgroundRes = R.color.unisex_bg;
                        break;
                    default:
                        backgroundRes = R.color.light_gray;
                        break;
                }
            } else {
                backgroundRes = R.color.light_gray;
            }
            
            // Set rounded background drawable with color tint
            textView.setBackgroundResource(R.drawable.rounded_target_audience_bg);
            textView.getBackground().setTint(context.getResources().getColor(backgroundRes, null));
        }
    }
}
