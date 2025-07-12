package com.example.perfumeshop.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.entities.Perfume;
import com.example.perfumeshop.presentation.activities.PerfumeDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class PerfumeAdapter extends RecyclerView.Adapter<PerfumeAdapter.PerfumeViewHolder> {
    private List<Perfume> perfumes;
    private Context context;

    public PerfumeAdapter(Context context) {
        this.context = context;
        this.perfumes = new ArrayList<>();
    }

    public void updatePerfumes(List<Perfume> newPerfumes) {
        this.perfumes.clear();
        if (newPerfumes != null) {
            this.perfumes.addAll(newPerfumes);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PerfumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_perfume_card, parent, false);
        return new PerfumeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerfumeViewHolder holder, int position) {
        Perfume perfume = perfumes.get(position);
        holder.bind(perfume);
    }

    @Override
    public int getItemCount() {
        return perfumes.size();
    }

    class PerfumeViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView imageViewPerfume;
        private TextView textViewName;
        private TextView textViewBrand;
        private TextView textViewPrice;
        private TextView textViewVolume;
        private TextView textViewTargetAudience;

        public PerfumeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewPerfume);
            imageViewPerfume = itemView.findViewById(R.id.imageViewPerfume);
            textViewName = itemView.findViewById(R.id.textViewPerfumeName);
            textViewBrand = itemView.findViewById(R.id.textViewBrand);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewVolume = itemView.findViewById(R.id.textViewVolume);
            textViewTargetAudience = itemView.findViewById(R.id.textViewTargetAudience);
        }

        public void bind(Perfume perfume) {
            textViewName.setText(perfume.getPerfumeName());
            textViewBrand.setText(perfume.getBrand() != null ? perfume.getBrand().getBrandName() : "Unknown");
            textViewPrice.setText(perfume.getFormattedPrice());
            textViewVolume.setText(perfume.getVolume() + "ml");
            textViewTargetAudience.setText(perfume.getTargetAudience());

            // Load image with Glide
            Glide.with(context)
                    .load(perfume.getUri())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder_perfume)
                            .error(R.drawable.placeholder_perfume)
                            .transform(new RoundedCorners(16)))
                    .into(imageViewPerfume);

            // Set click listener
            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PerfumeDetailsActivity.class);
                intent.putExtra("perfume_id", perfume.get_id());
                intent.putExtra("perfume_name", perfume.getPerfumeName());
                context.startActivity(intent);
            });
        }
    }
}
