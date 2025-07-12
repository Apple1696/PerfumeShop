package com.example.perfumeshop.presentation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.perfumeshop.R;
import com.example.perfumeshop.data.local.CartManager;
import com.example.perfumeshop.data.models.entities.CartItem;
import com.example.perfumeshop.data.models.entities.Perfume;
import com.example.perfumeshop.presentation.adapters.CommentsAdapter;
import com.example.perfumeshop.presentation.viewmodels.PerfumeDetailsViewModel;

public class PerfumeDetailsActivity extends AppCompatActivity {
    private PerfumeDetailsViewModel viewModel;
    private CommentsAdapter commentsAdapter;
    private CartManager cartManager;
    private Perfume currentPerfume;

    // Views
    private ImageView imageViewPerfume;
    private ImageView imageViewBack;
    private CardView buttonAddToCart;
    private TextView textViewPerfumeName;
    private TextView textViewBrand;
    private TextView textViewPrice;
    private TextView textViewVolume;
    private TextView textViewConcentration;
    private TextView textViewTargetAudience;
    private TextView textViewDescription;
    private TextView textViewIngredients;
    private RecyclerView recyclerViewComments;
    private TextView textViewNoComments;
    private ProgressBar progressBarContent;
    private ProgressBar progressBarImage;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfume_details);        initViews();
        setupViewModel();
        setupRecyclerView();
        setupObservers();
        setupClickListeners();
        
        // Initialize CartManager
        cartManager = CartManager.getInstance(this);

        // Get perfume ID from intent
        String perfumeId = getIntent().getStringExtra("perfume_id");
        if (perfumeId != null) {
            viewModel.fetchPerfumeDetails(perfumeId);
        } else {
            showError("No perfume ID provided");
            finish();
        }
    }    private void initViews() {
        imageViewPerfume = findViewById(R.id.imageViewPerfume);
        imageViewBack = findViewById(R.id.imageViewBack);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);
        textViewPerfumeName = findViewById(R.id.textViewPerfumeName);
        textViewBrand = findViewById(R.id.textViewBrand);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewVolume = findViewById(R.id.textViewVolume);
        textViewConcentration = findViewById(R.id.textViewConcentration);
        textViewTargetAudience = findViewById(R.id.textViewTargetAudience);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewIngredients = findViewById(R.id.textViewIngredients);
        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        textViewNoComments = findViewById(R.id.textViewNoComments);
        progressBarContent = findViewById(R.id.progressBarContent);
        progressBarImage = findViewById(R.id.progressBarImage);
        textViewError = findViewById(R.id.textViewError);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(PerfumeDetailsViewModel.class);
    }

    private void setupRecyclerView() {
        commentsAdapter = new CommentsAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewComments.setLayoutManager(layoutManager);
        recyclerViewComments.setAdapter(commentsAdapter);
    }

    private void setupObservers() {        viewModel.getPerfumeLiveData().observe(this, perfume -> {
            if (perfume != null) {
                currentPerfume = perfume;
                displayPerfumeDetails(perfume);
            }
        });

        viewModel.getLoadingLiveData().observe(this, isLoading -> {
            progressBarContent.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                showError(error);
            }
        });
    }    private void setupClickListeners() {
        imageViewBack.setOnClickListener(v -> finish());
        
        buttonAddToCart.setOnClickListener(v -> {
            if (currentPerfume != null) {
                addToCart();
            }
        });
    }
    
    private void addToCart() {
        CartItem cartItem = CartItem.fromPerfume(currentPerfume);
        cartManager.addToCart(cartItem);
        
        Toast.makeText(this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
    }

    private void displayPerfumeDetails(Perfume perfume) {
        // Hide error and loading states
        textViewError.setVisibility(View.GONE);
        progressBarContent.setVisibility(View.GONE);

        // Set basic information
        textViewPerfumeName.setText(perfume.getPerfumeName());
        textViewBrand.setText(perfume.getBrand() != null ? perfume.getBrand().getBrandName() : "Unknown Brand");
        textViewPrice.setText(perfume.getFormattedPrice());
        textViewVolume.setText(perfume.getVolume() + "ml");
        textViewConcentration.setText(perfume.getConcentration());
        textViewTargetAudience.setText(perfume.getTargetAudience());
        textViewDescription.setText(perfume.getDescription());
        textViewIngredients.setText(perfume.getIngredients());

        // Load perfume image
        loadPerfumeImage(perfume.getUri());

        // Setup comments
        setupComments(perfume);
    }    private void loadPerfumeImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            progressBarImage.setVisibility(View.VISIBLE);
            
            Glide.with(this)
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

    private void setupComments(Perfume perfume) {
        if (perfume.getComments() != null && !perfume.getComments().isEmpty()) {
            recyclerViewComments.setVisibility(View.VISIBLE);
            textViewNoComments.setVisibility(View.GONE);
            commentsAdapter.updateComments(perfume.getComments());
        } else {
            recyclerViewComments.setVisibility(View.GONE);
            textViewNoComments.setVisibility(View.VISIBLE);
        }
    }

    private void showError(String message) {
        textViewError.setText(message);
        textViewError.setVisibility(View.VISIBLE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
