package com.example.perfumeshop.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.perfumeshop.data.api.PerfumeRepository;
import com.example.perfumeshop.data.models.entities.Perfume;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfumeDetailsViewModel extends ViewModel {
    private PerfumeRepository repository;
    private MutableLiveData<Perfume> perfumeLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Boolean> loadingLiveData;

    public PerfumeDetailsViewModel() {
        repository = new PerfumeRepository();
        perfumeLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
    }

    public LiveData<Perfume> getPerfumeLiveData() {
        return perfumeLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public void fetchPerfumeDetails(String perfumeId) {
        loadingLiveData.setValue(true);
        repository.fetchPerfumeDetail(perfumeId, new Callback<Perfume>() {
            @Override
            public void onResponse(Call<Perfume> call, Response<Perfume> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    perfumeLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue("Failed to load perfume details");
                }
            }

            @Override
            public void onFailure(Call<Perfume> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }
}
