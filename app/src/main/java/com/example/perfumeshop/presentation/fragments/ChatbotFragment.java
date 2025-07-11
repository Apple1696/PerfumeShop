package com.example.perfumeshop.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.entities.ChatMessage;
import com.example.perfumeshop.presentation.adapters.ChatMessageAdapter;
import com.example.perfumeshop.presentation.viewmodels.ChatbotViewModel;

public class ChatbotFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private ChatMessageAdapter chatAdapter;
    private ChatbotViewModel viewModel;

    public ChatbotFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatbot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModel();
        setupRecyclerView();
        setupClickListeners();
        observeViewModel();

        // Add welcome message
        addWelcomeMessage();
    }

    private void initViews(View view) {
        chatRecyclerView = view.findViewById(R.id.chat_messages_recycler_view);
        messageInput = view.findViewById(R.id.message_input);
        sendButton = view.findViewById(R.id.send_button);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ChatbotViewModel.class);
    }

    private void setupRecyclerView() {
        chatAdapter = new ChatMessageAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> sendMessage());

        // Send message on Enter key press
        messageInput.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }

    private void observeViewModel() {
        viewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            if (messages != null) {
                chatAdapter.setMessages(messages);
                scrollToBottom();
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                sendButton.setEnabled(!isLoading);
                if (isLoading) {
                    // Show typing indicator or loading message
                    // You can add a loading message here if needed
                }
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                viewModel.clearError();
            }
        });
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (!message.isEmpty()) {
            viewModel.sendMessage(message);
            messageInput.setText("");
        }
    }

    private void addWelcomeMessage() {
        ChatMessage welcomeMessage = new ChatMessage("assistant",
            "Xin chào! Tôi là chatbot tư vấn nước hoa. Tôi có thể giúp bạn tìm hiểu về các loại nước hoa, tư vấn lựa chọn phù hợp với sở thích và ngân sách của bạn. Hãy cho tôi biết bạn muốn tìm hiểu gì về nước hoa nhé!");
        chatAdapter.addMessage(welcomeMessage);
        scrollToBottom();
    }

    private void scrollToBottom() {
        if (chatAdapter.getItemCount() > 0) {
            chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
        }
    }
}