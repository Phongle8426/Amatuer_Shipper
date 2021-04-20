package com.example.AmateurShipper;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewAdapterClass>{
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Context mContext;
    private List<MessageObject> mChat;
    public ChatAdapter(Context mcontext, List<MessageObject> mchat) {
        this.mContext = mcontext;
        this.mChat = mchat;
    }

    public void addItem(int position,MessageObject addList ) {
        mChat.add(position, addList);
        notifyItemInserted(position);
    }


    @NonNull
    @Override

    public ChatAdapter.ViewAdapterClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
            ChatAdapter.ViewAdapterClass viewAdapterClass = new ChatAdapter.ViewAdapterClass(view);
            return viewAdapterClass;
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
            ChatAdapter.ViewAdapterClass viewAdapterClass = new ChatAdapter.ViewAdapterClass(view);
            return viewAdapterClass;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewAdapterClass holder, int position) {
        MessageObject messageObject = mChat.get(position);
        if (!messageObject.getMessage().equals("")) {
            holder.showMessage.setVisibility(View.VISIBLE);
            holder.showImageMessage.setVisibility(View.GONE);
            holder.showMessage.setText(messageObject.getMessage());
        } else {
            holder.showImageMessage.setVisibility(View.VISIBLE);
            holder.showMessage.setVisibility(View.GONE);
            Picasso.get().load(messageObject.getImgmessage()).fit().into(holder.showImageMessage);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }
    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        public TextView showMessage;
        public ImageView showImageMessage;

        public ViewAdapterClass(@NonNull final View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.show_message);
            showImageMessage = itemView.findViewById(R.id.show_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mChat.get(position).getId().equals(user.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else
            return MSG_TYPE_LEFT;
    }
}

