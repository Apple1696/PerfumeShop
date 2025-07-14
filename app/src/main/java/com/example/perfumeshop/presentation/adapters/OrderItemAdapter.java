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
import com.example.perfumeshop.data.models.entities.OrderItem;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private Context context;
    private List<OrderItem> orderItems;

    public OrderItemAdapter(Context context, List<OrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    public void updateOrderItems(List<OrderItem> newOrderItems) {
        this.orderItems = newOrderItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        holder.bind(orderItem);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPerfume;
        TextView textViewPerfumeName;
        TextView textViewBrandName;
        TextView textViewConcentration;
        TextView textViewQuantity;
        TextView textViewPrice;
        TextView textViewTotalPrice;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPerfume = itemView.findViewById(R.id.imageViewPerfume);
            textViewPerfumeName = itemView.findViewById(R.id.textViewPerfumeName);
            textViewBrandName = itemView.findViewById(R.id.textViewBrandName);
            textViewConcentration = itemView.findViewById(R.id.textViewConcentration);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewTotalPrice = itemView.findViewById(R.id.textViewTotalPrice);
        }

        public void bind(OrderItem orderItem) {
            if (orderItem.getPerfume() != null) {
                textViewPerfumeName.setText(orderItem.getPerfume().getPerfumeName());
                textViewConcentration.setText(orderItem.getPerfume().getConcentration());
                textViewPrice.setText(orderItem.getPerfume().getFormattedPrice());

                // Load perfume image
                Glide.with(context)
                        .load(orderItem.getPerfume().getUri())
                        .placeholder(R.drawable.placeholder_perfume)
                        .error(R.drawable.placeholder_perfume)
                        .into(imageViewPerfume);

                // Brand name
                if (orderItem.getPerfume().getBrand() != null) {
                    textViewBrandName.setText(orderItem.getPerfume().getBrand().getBrandName());
                } else {
                    textViewBrandName.setText("Unknown Brand");
                }
            }

            textViewQuantity.setText("Qty: " + orderItem.getQuantity());
            textViewTotalPrice.setText(orderItem.getFormattedTotalPrice());
        }
    }
}
