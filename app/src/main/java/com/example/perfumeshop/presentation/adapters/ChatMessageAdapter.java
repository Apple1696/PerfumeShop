package com.example.perfumeshop.presentation.adapters;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.entities.ChatMessage;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_ASSISTANT = 2;

    private List<ChatMessage> messages;

    public ChatMessageAdapter() {
        this.messages = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        return "user".equals(message.getRole()) ? VIEW_TYPE_USER : VIEW_TYPE_ASSISTANT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message_user, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message_assistant, parent, false);
            return new AssistantMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(message);
        } else if (holder instanceof AssistantMessageViewHolder) {
            ((AssistantMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    public List<ChatMessage> getMessages() {
        return new ArrayList<>(messages);
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageText;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
        }

        public void bind(ChatMessage message) {
            messageText.setText(message.getContent());
        }
    }

    static class AssistantMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageText;

        public AssistantMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
        }

        public void bind(ChatMessage message) {
            String content = message.getContent();

            // Format the message to make "perfume" bold (case-insensitive)
            String formattedContent = formatPerfumeText(content);

            // Use HTML formatting to display bold text
            Spanned spannedText = Html.fromHtml(formattedContent, Html.FROM_HTML_MODE_LEGACY);
            messageText.setText(spannedText);
        }

        private String formatPerfumeText(String text) {
            if (text == null) return "";

            // First handle heading formatting: ### text ### or ### text -> <h3>text</h3>
            text = text.replaceAll("###\\s*(.*?)\\s*###", "<h3>$1</h3>");
            text = text.replaceAll("###\\s*(.*?)(?=\\n|$)", "<h3>$1</h3>");

            // Then handle bold formatting: **text** -> <b>text</b>
            text = text.replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>");

            // Finally handle italic formatting: *text* -> <i>text</i>
            // Use negative lookbehind and lookahead to avoid matching already processed bold text
            text = text.replaceAll("(?<!\\*)\\*(?!\\*)(.*?)(?<!\\*)\\*(?!\\*)", "<i>$1</i>");

            return text;
        }
    }
}
