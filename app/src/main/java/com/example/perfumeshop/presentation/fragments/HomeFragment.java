package com.example.perfumeshop.presentation.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.entities.Brand;
import com.example.perfumeshop.data.models.entities.Perfume;
import com.example.perfumeshop.presentation.adapters.BrandSpinnerAdapter;
import com.example.perfumeshop.presentation.adapters.PerfumeAdapter;
import com.example.perfumeshop.presentation.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    private PerfumeAdapter perfumeAdapter;
    private BrandSpinnerAdapter brandAdapter;
    
    private EditText editTextSearch;
    private Spinner spinnerBrands;
    private ImageView imageViewClearFilter;
    private RecyclerView recyclerViewPerfumes;
    private ProgressBar progressBar;
    private TextView textViewError;
    private TextView textViewEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupViewModel();
        setupRecyclerView();
        setupSearchAndFilter();
        setupObservers();
        
        // Fetch data
        viewModel.fetchData();
    }

    private void initViews(View view) {
        editTextSearch = view.findViewById(R.id.editTextSearch);
        spinnerBrands = view.findViewById(R.id.spinnerBrands);
        imageViewClearFilter = view.findViewById(R.id.imageViewClearFilter);
        recyclerViewPerfumes = view.findViewById(R.id.recyclerViewPerfumes);
        progressBar = view.findViewById(R.id.progressBar);
        textViewError = view.findViewById(R.id.textViewError);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private void setupRecyclerView() {
        perfumeAdapter = new PerfumeAdapter(getContext());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewPerfumes.setLayoutManager(layoutManager);
        recyclerViewPerfumes.setAdapter(perfumeAdapter);
    }

    private void setupSearchAndFilter() {
        // Setup search functionality
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.searchPerfumes(s.toString());
            }
        });

        // Setup clear filter button
        imageViewClearFilter.setOnClickListener(v -> {
            editTextSearch.setText("");
            spinnerBrands.setSelection(0);
            viewModel.clearFilters();
        });
    }

    private void setupObservers() {
        // Observe perfumes data
        viewModel.getPerfumesLiveData().observe(getViewLifecycleOwner(), perfumes -> {
            if (perfumes != null) {
                viewModel.updatePerfumesList(perfumes);
            }
        });

        // Observe filtered perfumes
        viewModel.getFilteredPerfumesLiveData().observe(getViewLifecycleOwner(), perfumes -> {
            if (perfumes != null) {
                perfumeAdapter.updatePerfumes(perfumes);
                updateEmptyState(perfumes.isEmpty());
            }
        });

        // Observe brands data
        viewModel.getBrandsLiveData().observe(getViewLifecycleOwner(), brands -> {
            if (brands != null) {
                setupBrandSpinner(brands);
            }
        });

        // Observe loading state
        viewModel.getLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Observe error state
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                textViewError.setText(error);
                textViewError.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            } else {
                textViewError.setVisibility(View.GONE);
            }
        });
    }

    private void setupBrandSpinner(List<Brand> brands) {
        List<Brand> brandList = new ArrayList<>();
        // Add "All Brands" option at the beginning
        Brand allBrands = new Brand("", "All Brands");
        brandList.add(allBrands);
        brandList.addAll(brands);

        brandAdapter = new BrandSpinnerAdapter(getContext(), brandList);
        spinnerBrands.setAdapter(brandAdapter);

        spinnerBrands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Brand selectedBrand = brandList.get(position);
                if (position == 0) {
                    // "All Brands" selected
                    viewModel.filterByBrand("");
                } else {
                    viewModel.filterByBrand(selectedBrand.getBrandName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateEmptyState(boolean isEmpty) {
        textViewEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerViewPerfumes.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}