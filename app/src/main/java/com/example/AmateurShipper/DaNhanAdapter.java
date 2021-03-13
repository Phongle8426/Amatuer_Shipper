package com.example.AmateurShipper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class DaNhanAdapter extends RecyclerView.Adapter {
    List<PostObject> orderDaNhan;

    public DaNhanAdapter(List<PostObject> orderDaNhan){
        this.orderDaNhan = orderDaNhan;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab_nhan,parent,false);
        DaNhanAdapter.ViewAdapterClass viewAdapterClass = new DaNhanAdapter.ViewAdapterClass(view);
        return viewAdapterClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DaNhanAdapter.ViewAdapterClass viewAdapterClass = (DaNhanAdapter.ViewAdapterClass)holder;
        PostObject postObject =orderDaNhan.get(position);
        viewAdapterClass.name_poster.setText(postObject.getName_poster());
        viewAdapterClass.start_post.setText(postObject.getStart_post());
        viewAdapterClass.end_post.setText(postObject.getEnd_post());

    }

    @Override
    public int getItemCount() {
        return orderDaNhan.size();
    }

    public class ViewAdapterClass extends RecyclerView.ViewHolder{
        TextView name_poster,start_post,end_post;
        public ViewAdapterClass(@NonNull final View itemView) {
            super(itemView);
            name_poster = itemView.findViewById(R.id.name_poster_tab_nhan);
            start_post = itemView.findViewById(R.id.txt_start_place_tab_nhan);
            end_post = itemView.findViewById(R.id.txt_end_place_tab_nhan);
        }
    }
}
