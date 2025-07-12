package com.example.perfumeshop.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.perfumeshop.data.api.PerfumeRepository;
import com.example.perfumeshop.data.models.entities.Perfume;
import com.example.perfumeshop.data.models.request.CommentRequest;
import com.example.perfumeshop.data.models.response.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfumeDetailsViewModel extends ViewModel {
    private PerfumeRepository repository;
    private MutableLiveData<Perfume> perfumeLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Boolean> loadingLiveData;
    private MutableLiveData<Boolean> commentCreateSuccessLiveData;

    public PerfumeDetailsViewModel() {
        repository = new PerfumeRepository();
        perfumeLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
        commentCreateSuccessLiveData = new MutableLiveData<>();
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

    public LiveData<Boolean> getCommentCreateSuccessLiveData() {
        return commentCreateSuccessLiveData;
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

    public void createComment(String perfumeId, int rating, String content) {
        CommentRequest commentRequest = new CommentRequest(rating, content);
        repository.createComment(perfumeId, commentRequest, new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentCreateSuccessLiveData.setValue(true);
                    // Refresh perfume details to get updated comments
                    fetchPerfumeDetails(perfumeId);
                } else {
                    errorLiveData.setValue("Failed to create comment");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }
}
