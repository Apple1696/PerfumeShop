package com.example.perfumeshop.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.perfumeshop.data.api.PerfumeRepository;
import com.example.perfumeshop.data.models.entities.Brand;
import com.example.perfumeshop.data.models.entities.Perfume;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private PerfumeRepository repository;
    private MutableLiveData<List<Perfume>> filteredPerfumesLiveData;
    private List<Perfume> allPerfumes;
    private String currentSearchQuery = "";
    private String currentBrandFilter = "";

    public HomeViewModel() {
        repository = new PerfumeRepository();
        filteredPerfumesLiveData = new MutableLiveData<>();
        allPerfumes = new ArrayList<>();
    }

    public LiveData<List<Perfume>> getPerfumesLiveData() {
        return repository.getPerfumesLiveData();
    }

    public LiveData<List<Brand>> getBrandsLiveData() {
        return repository.getBrandsLiveData();
    }

    public LiveData<List<Perfume>> getFilteredPerfumesLiveData() {
        return filteredPerfumesLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return repository.getErrorLiveData();
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return repository.getLoadingLiveData();
    }

    public void fetchData() {
        repository.fetchPerfumes();
        repository.fetchBrands();
    }

    public void updatePerfumesList(List<Perfume> perfumes) {
        this.allPerfumes = perfumes;
        applyFilters();
    }

    public void searchPerfumes(String query) {
        currentSearchQuery = query.toLowerCase().trim();
        applyFilters();
    }

    public void filterByBrand(String brandName) {
        currentBrandFilter = brandName;
        applyFilters();
    }

    public void clearFilters() {
        currentSearchQuery = "";
        currentBrandFilter = "";
        applyFilters();
    }

    private void applyFilters() {
        List<Perfume> filtered = new ArrayList<>();

        for (Perfume perfume : allPerfumes) {
            boolean matchesSearch = currentSearchQuery.isEmpty() ||
                    perfume.getPerfumeName().toLowerCase().contains(currentSearchQuery) ||
                    perfume.getDescription().toLowerCase().contains(currentSearchQuery);

            boolean matchesBrand = currentBrandFilter.isEmpty() ||
                    (perfume.getBrand() != null && perfume.getBrand().getBrandName().equals(currentBrandFilter));

            if (matchesSearch && matchesBrand) {
                filtered.add(perfume);
            }
        }

        filteredPerfumesLiveData.setValue(filtered);
    }
}
