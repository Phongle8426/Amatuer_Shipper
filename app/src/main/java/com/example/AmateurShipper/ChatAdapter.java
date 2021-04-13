package com.example.AmateurShipper;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewAdapterClass>{
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    SharedPreferences sharedPreferences;
    String id_user;
    private List<MessageObject> mChat;
    public ChatAdapter(Context mcontext, List<MessageObject> mchat) {
        this.mContext = mcontext;
        this.mChat = mchat;
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
        loadUserId();
        MessageObject messageObject = mChat.get(position);
        holder.showMessage.setText(messageObject.getMessage());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }
    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        public TextView showMessage;

        public ViewAdapterClass(@NonNull final View itemView) {
            super(itemView);
            sharedPreferences = mContext.getSharedPreferences(LoginActivity.MyPREFERENCESIDUSER, Context.MODE_PRIVATE );
            showMessage = itemView.findViewById(R.id.show_message);
                }
        }

    @Override
    public int getItemViewType(int position) {
        if(mChat.get(position).getId().equals(id_user)){
            return MSG_TYPE_RIGHT;
        }
        else
            return MSG_TYPE_LEFT;
    }
    public void loadUserId(){
        id_user = sharedPreferences.getString(LoginActivity.IDUSER,"");
    }
}

