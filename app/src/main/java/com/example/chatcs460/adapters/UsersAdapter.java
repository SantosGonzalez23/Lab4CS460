package com.example.chatcs460.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatcs460.databinding.ItemContainerUserBinding;
import com.example.chatcs460.listeners.UserListener;
import com.example.chatcs460.models.User;

import java.util.List;

/**
 * Adapter class for displaying a list of users in a RecyclerView.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> users;
    private final UserListener userListener;

    /**
     * Constructor for initializing the adapter with a list of users and a user listener.
     * @param users the list of users to display.
     * @param userListener the listener for handling user item clicks.
     */
    public UsersAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * ViewHolder class for binding user data to a single item view.
     */
    class UserViewHolder extends RecyclerView.ViewHolder {
        ItemContainerUserBinding binding;

        /**
         * Constructor for initializing the ViewHolder with the binding of a user item.
         * @param itemContainerUserBinding the binding for the user item.
         */
        public UserViewHolder(ItemContainerUserBinding itemContainerUserBinding) {
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }

        /**
         * Binds user data to the views.
         * @param user the user data to display.
         */
        void setUserData(User user) {
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));

            binding.getRoot().setOnClickListener(view -> userListener.onUserClicked(user));
        }
    }

    /**
     * Decodes a base64-encoded image string into a Bitmap.
     * @param encodedImage the base64-encoded image string.
     * @return the decoded Bitmap.
     */
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
