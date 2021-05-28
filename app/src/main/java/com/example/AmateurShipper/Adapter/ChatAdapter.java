package com.example.AmateurShipper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.AmateurShipper.Model.MessageObject;
import com.example.AmateurShipper.R;
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
            holder.timeMessage.setText(messageObject.getTimemessage());
        } else {
            holder.showImageMessage.setVisibility(View.VISIBLE);
            holder.showMessage.setVisibility(View.GONE);
            holder.timeMessage.setText(messageObject.getTimemessage());
            Picasso.get().load(messageObject.getImgmessage()).fit().into(holder.showImageMessage);
        }

        if (position == mChat.size()-1){
            if (messageObject.getIsseen().equals("1")){
                //holder.seenMessage.setText("Da xem");
                holder.seen.setImageResource(R.drawable.background_seen_chat);
            }else{
                holder.seen.setImageResource(R.drawable.background_notseen_chat);
            }
        }else{
            holder.seen.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }
    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        public TextView showMessage,seenMessage,timeMessage;
        public ImageView showImageMessage,seen;
        de.hdodenhof.circleimageview.CircleImageView avt;


        public ViewAdapterClass(@NonNull final View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.show_message);
            showImageMessage = itemView.findViewById(R.id.show_image);
            //seenMessage = itemView.findViewById(R.id.tv_seen);
            timeMessage = itemView.findViewById(R.id.time_message);
            seen = itemView.findViewById(R.id.seen);
            avt = itemView.findViewById(R.id.avt_shop);
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

