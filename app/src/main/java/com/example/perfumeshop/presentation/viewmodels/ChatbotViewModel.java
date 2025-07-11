package com.example.perfumeshop.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.perfumeshop.data.api.ApiClient;
import com.example.perfumeshop.data.api.ChatbotApiService;
import com.example.perfumeshop.data.models.entities.ChatMessage;
import com.example.perfumeshop.data.models.request.ChatRequest;
import com.example.perfumeshop.data.models.response.ChatResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotViewModel extends ViewModel {
    private ChatbotApiService apiService;
    private MutableLiveData<List<ChatMessage>> messagesLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;
    private List<ChatMessage> conversationHistory;

    public ChatbotViewModel() {
        messagesLiveData = new MutableLiveData<>();
        isLoadingLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        conversationHistory = new ArrayList<>();

        // Use your existing ApiClient
        apiService = ApiClient.getRetrofitInstance().create(ChatbotApiService.class);

        // Initialize with empty list
        messagesLiveData.setValue(new ArrayList<>());
    }

    public LiveData<List<ChatMessage>> getMessages() {
        return messagesLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void sendMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        // Add user message to conversation
        ChatMessage userMessage = new ChatMessage("user", message.trim());
        addMessageToConversation(userMessage);

        // Add loading message
        ChatMessage loadingMessage = new ChatMessage("assistant", "Đang suy nghĩ...");
        addMessageToConversation(loadingMessage);

        // Show loading state
        isLoadingLiveData.setValue(true);

        // Create request
        ChatRequest request = new ChatRequest(message.trim(), new ArrayList<>(conversationHistory));

        // Make API call
        Call<ChatResponse> call = apiService.sendMessage(request);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                isLoadingLiveData.setValue(false);

                // Remove loading message and replace with actual response
                removeLastMessage(); // Remove the loading message

                if (response.isSuccessful() && response.body() != null) {
                    ChatResponse chatResponse = response.body();

                    if (chatResponse.isSuccess()) {
                        // Add assistant message to conversation
                        ChatMessage assistantMessage = new ChatMessage("assistant", chatResponse.getMessage());
                        addMessageToConversation(assistantMessage);

                        // Update conversation history with response
                        if (chatResponse.getConversationHistory() != null) {
                            conversationHistory.clear();
                            conversationHistory.addAll(chatResponse.getConversationHistory());
                        }
                    } else {
                        // Add error message instead of loading message
                        ChatMessage errorMessage = new ChatMessage("assistant", "Xin lỗi, tôi không thể trả lời được. Vui lòng thử lại.");
                        addMessageToConversation(errorMessage);
                        errorLiveData.setValue("Failed to get response from chatbot");
                    }
                } else {
                    // Add error message instead of loading message
                    ChatMessage errorMessage = new ChatMessage("assistant", "Có lỗi kết nối. Vui lòng thử lại sau.");
                    addMessageToConversation(errorMessage);
                    errorLiveData.setValue("Network error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                isLoadingLiveData.setValue(false);

                // Remove loading message and add error message
                removeLastMessage();
                ChatMessage errorMessage = new ChatMessage("assistant", "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.");
                addMessageToConversation(errorMessage);
                errorLiveData.setValue("Connection error: " + t.getMessage());
            }
        });
    }

    private void addMessageToConversation(ChatMessage message) {
        List<ChatMessage> currentMessages = messagesLiveData.getValue();
        if (currentMessages != null) {
            List<ChatMessage> updatedMessages = new ArrayList<>(currentMessages);
            updatedMessages.add(message);
            messagesLiveData.setValue(updatedMessages);
        }
    }

    private void removeLastMessage() {
        List<ChatMessage> currentMessages = messagesLiveData.getValue();
        if (currentMessages != null && !currentMessages.isEmpty()) {
            List<ChatMessage> updatedMessages = new ArrayList<>(currentMessages);
            updatedMessages.remove(updatedMessages.size() - 1);
            messagesLiveData.setValue(updatedMessages);
        }
    }

    public void clearError() {
        errorLiveData.setValue(null);
    }
}
