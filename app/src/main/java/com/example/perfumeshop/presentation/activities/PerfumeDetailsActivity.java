package com.example.perfumeshop.presentation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.example.perfumeshop.data.api.TokenManager;
import com.example.perfumeshop.data.local.CartManager;
import com.example.perfumeshop.data.models.entities.CartItem;
import com.example.perfumeshop.data.models.entities.Comment;
import com.example.perfumeshop.data.models.entities.Perfume;
import com.example.perfumeshop.data.models.entities.User;
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
    private TextView textViewError;    // Comment form views
    private ImageView[] starViews;
    private int selectedRating = 5; // Default rating
    private EditText editTextComment;
    private CardView buttonSubmitComment;
    private CardView commentFormContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfume_details);
        
        initViews();
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
    }

    private void initViews() {
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
        progressBarImage = findViewById(R.id.progressBarImage);        textViewError = findViewById(R.id.textViewError);
        
        // Comment form views
        starViews = new ImageView[5];
        starViews[0] = findViewById(R.id.star1);
        starViews[1] = findViewById(R.id.star2);
        starViews[2] = findViewById(R.id.star3);
        starViews[3] = findViewById(R.id.star4);        starViews[4] = findViewById(R.id.star5);
        editTextComment = findViewById(R.id.editTextComment);
        buttonSubmitComment = findViewById(R.id.buttonSubmitComment);
        commentFormContainer = findViewById(R.id.commentFormContainer);
        
        setupStarRating();
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

    private void setupObservers() {
        viewModel.getPerfumeLiveData().observe(this, perfume -> {
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
        });        viewModel.getCommentCreateSuccessLiveData().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Comment submitted successfully!", Toast.LENGTH_SHORT).show();
                // Clear the form
                editTextComment.setText("");
                setRating(5); // Reset to 5 stars
            }
        });
    }

    private void setupClickListeners() {
        imageViewBack.setOnClickListener(v -> finish());
        
        buttonAddToCart.setOnClickListener(v -> {
            if (currentPerfume != null) {
                addToCart();
            }
        });
        
        buttonSubmitComment.setOnClickListener(v -> {
            if (currentPerfume != null) {
                submitComment();
            }
        });
    }
    
    private void addToCart() {
        CartItem cartItem = CartItem.fromPerfume(currentPerfume);
        cartManager.addToCart(cartItem);
        
        Toast.makeText(this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
    }    
      private void submitComment() {
        String comment = editTextComment.getText().toString().trim();
        if (comment.isEmpty()) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedRating == 0) {
            Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
            return;
        }
        
        viewModel.createComment(currentPerfume.get_id(), selectedRating, comment);
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
        
        // Set background color for target audience
        setTargetAudienceBackground(textViewTargetAudience, perfume.getTargetAudience());
        
        textViewDescription.setText(perfume.getDescription());
        textViewIngredients.setText(perfume.getIngredients());        // Load perfume image
        loadPerfumeImage(perfume.getUri());

        // Check if user has already commented and show/hide comment form
        checkCommentFormVisibility(perfume);

        // Setup comments
        setupComments(perfume);
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
        textView.getBackground().setTint(getResources().getColor(backgroundRes, null));
    }private void loadPerfumeImage(String imageUrl) {
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
    }    private void showError(String message) {
        textViewError.setText(message);
        textViewError.setVisibility(View.VISIBLE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    private void setupStarRating() {
        for (int i = 0; i < starViews.length; i++) {
            final int starIndex = i + 1; // 1-based rating
            starViews[i].setOnClickListener(v -> setRating(starIndex));
        }
    }
      private void setRating(int rating) {
        selectedRating = rating;
        for (int i = 0; i < starViews.length; i++) {
            if (i < rating) {
                starViews[i].setImageResource(R.drawable.ic_star_filled);
            } else {
                starViews[i].setImageResource(R.drawable.ic_star_empty);
            }
        }
    }
    
    private void checkCommentFormVisibility(Perfume perfume) {
        // Get current user
        User currentUser = TokenManager.getCurrentUser(this);
        
        // If user is not logged in, hide the comment form
        if (currentUser == null || currentUser.getEmail() == null) {
            commentFormContainer.setVisibility(View.GONE);
            return;
        }
        
        // Check if current user has already commented
        boolean hasCommented = false;
        if (perfume.getComments() != null) {
            for (Comment comment : perfume.getComments()) {
                if (comment.getAuthor() != null && 
                    comment.getAuthor().getEmail() != null && 
                    comment.getAuthor().getEmail().equals(currentUser.getEmail())) {
                    hasCommented = true;
                    break;
                }
            }
        }
        
        // Show or hide comment form based on whether user has commented
        if (hasCommented) {
            commentFormContainer.setVisibility(View.GONE);
        } else {
            commentFormContainer.setVisibility(View.VISIBLE);
        }
    }
}
