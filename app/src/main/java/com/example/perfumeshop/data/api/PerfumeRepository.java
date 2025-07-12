package com.example.perfumeshop.data.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.perfumeshop.data.models.entities.Brand;
import com.example.perfumeshop.data.models.entities.Perfume;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfumeRepository {
    private ApiService apiService;
    private MutableLiveData<List<Perfume>> perfumesLiveData;
    private MutableLiveData<List<Brand>> brandsLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Boolean> loadingLiveData;

    public PerfumeRepository() {
        this.apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        this.perfumesLiveData = new MutableLiveData<>();
        this.brandsLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.loadingLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Perfume>> getPerfumesLiveData() {
        return perfumesLiveData;
    }

    public LiveData<List<Brand>> getBrandsLiveData() {
        return brandsLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public void fetchPerfumes() {
        loadingLiveData.setValue(true);
        Call<List<Perfume>> call = apiService.getPerfumes();
        call.enqueue(new Callback<List<Perfume>>() {
            @Override
            public void onResponse(Call<List<Perfume>> call, Response<List<Perfume>> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    perfumesLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue("Failed to fetch perfumes");
                }
            }

            @Override
            public void onFailure(Call<List<Perfume>> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void fetchBrands() {
        Call<List<Brand>> call = apiService.getBrands();
        call.enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    brandsLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue("Failed to fetch brands");
                }
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable t) {
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void fetchPerfumeDetail(String perfumeId, Callback<Perfume> callback) {
        Call<Perfume> call = apiService.getPerfumeDetail(perfumeId);
        call.enqueue(callback);
    }
}
