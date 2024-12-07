package com.example.chatcs460.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatcs460.databinding.ItemContainerReceivedMessageBinding;
import com.example.chatcs460.databinding.ItemContainerSentMessageBinding;
import com.example.chatcs460.models.ChatMessage;

import java.util.List;

/**
 * Adapter class for displaying a list of chat messages in a RecyclerView.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Bitmap receiverProfileImage;
    private final List<ChatMessage> chatMessages;
    private final String sendId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    /**
     * Constructor for initializing the adapter with chat messages, receiver's profile image, and sender ID.
     * @param chatMessages the list of chat messages to display.
     * @param receiverProfileImage the profile image of the message receiver.
     * @param sendId the ID of the sender.
     */
    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String sendId) {
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.sendId = sendId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(ItemContainerSentMessageBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ReceiverMessageViewHolder(ItemContainerReceivedMessageBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        } else {
            ((ReceiverMessageViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(sendId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    /**
     * ViewHolder class for binding sent message data to a single item view.
     */
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSentMessageBinding binding;

        /**
         * Constructor for initializing the ViewHolder with the binding of a sent message item.
         * @param itemContainerSentMessageBinding the binding for the sent message item.
         */
        public SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        /**
         * Binds sent message data to the views.
         * @param chatMessage the chat message data to display.
         */
        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    /**
     * ViewHolder class for binding received message data to a single item view.
     */
    static class ReceiverMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerReceivedMessageBinding binding;

        /**
         * Constructor for initializing the ViewHolder with the binding of a received message item.
         * @param itemContainerReceivedMessageBinding the binding for the received message item.
         */
        public ReceiverMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        /**
         * Binds received message data to the views.
         * @param chatMessage the chat message data to display.
         * @param receiverProfileImage the profile image of the message receiver.
         */
        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            binding.imageProfile.setImageBitmap(receiverProfileImage);
        }
    }
}
