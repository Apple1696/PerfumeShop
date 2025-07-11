package com.example.perfumeshop.data.models.response;

import com.example.perfumeshop.data.models.entities.ChatMessage;
import java.util.List;

public class ChatResponse {
    private boolean success;
    private String message;
    private List<ChatMessage> conversationHistory;

    public ChatResponse() {}

    public ChatResponse(boolean success, String message, List<ChatMessage> conversationHistory) {
        this.success = success;
        this.message = message;
        this.conversationHistory = conversationHistory;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ChatMessage> getConversationHistory() {
        return conversationHistory;
    }

    public void setConversationHistory(List<ChatMessage> conversationHistory) {
        this.conversationHistory = conversationHistory;
    }
}
