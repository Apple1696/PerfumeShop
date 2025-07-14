package com.example.perfumeshop.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.entities.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnOrderClickListener onOrderClickListener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.onOrderClickListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        if (order != null) {
            holder.bind(order);
        }
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textViewOrderCode;
        TextView textViewOrderDate;
        TextView textViewTotalPrice;
        TextView textViewOrderStatus;
        TextView textViewPaymentMethod;
        TextView textViewItemCount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewOrder);
            textViewOrderCode = itemView.findViewById(R.id.textViewOrderCode);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDate);
            textViewTotalPrice = itemView.findViewById(R.id.textViewTotalPrice);
            textViewOrderStatus = itemView.findViewById(R.id.textViewOrderStatus);
            textViewPaymentMethod = itemView.findViewById(R.id.textViewPaymentMethod);
            textViewItemCount = itemView.findViewById(R.id.textViewItemCount);

            cardView.setOnClickListener(v -> {
                if (onOrderClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onOrderClickListener.onOrderClick(orderList.get(position));
                    }
                }
            });
        }

        public void bind(Order order) {
            if (order == null) return;

            // Safe text setting with null checks
            textViewOrderCode.setText("Order #" + (order.getOrderCode() > 0 ? order.getOrderCode() : "N/A"));

            String formattedPrice = order.getFormattedTotalPrice();
            textViewTotalPrice.setText(formattedPrice != null ? formattedPrice : "0 VND");

            String status = order.getOrderStatus();
            textViewOrderStatus.setText(status != null ? status : "Unknown");

            String paymentMethod = order.getPaymentMethod();
            textViewPaymentMethod.setText(paymentMethod != null ? paymentMethod : "N/A");

            // Format date with better error handling
            Date orderDate = order.getCreatedAtAsDate();
            if (orderDate != null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    textViewOrderDate.setText(dateFormat.format(orderDate));
                } catch (Exception e) {
                    textViewOrderDate.setText("N/A");
                }
            } else {
                textViewOrderDate.setText("N/A");
            }

            // Item count with null check
            int itemCount = 0;
            if (order.getOrderItems() != null) {
                itemCount = order.getOrderItems().size();
            }
            textViewItemCount.setText(itemCount + (itemCount == 1 ? " item" : " items"));

            // Set status color with null and context checks
            if (context != null && status != null) {
                try {
                    switch (status.toLowerCase()) {
                        case "pending":
                            textViewOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case "completed":
                        case "delivered":
                            textViewOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                            break;
                        case "cancelled":
                            textViewOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                            break;
                        default:
                            textViewOrderStatus.setTextColor(context.getResources().getColor(android.R.color.black));
                            break;
                    }
                } catch (Exception e) {
                    // Fallback to default color if there's any issue
                    textViewOrderStatus.setTextColor(context.getResources().getColor(android.R.color.black));
                }
            }
        }
    }
}
