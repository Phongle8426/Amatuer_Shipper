package com.example.AmateurShipper.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;
import com.example.AmateurShipper.Model.PostObject;
import com.example.AmateurShipper.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DaNhanAdapter extends RecyclerView.Adapter {
    List<PostObject> orderDaNhan;
    private statusInterfaceRecyclerView statusInterfaceRecyclerView;

    public DaNhanAdapter(List<PostObject> orderDaNhan,statusInterfaceRecyclerView statusInterfaceRecyclerView){
        this.orderDaNhan = orderDaNhan;
        this.statusInterfaceRecyclerView = statusInterfaceRecyclerView;
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
        viewAdapterClass.name_poster.setText(postObject.getTen_nguoi_gui());
        viewAdapterClass.start_post.setText(postObject.getNoi_nhan());
        viewAdapterClass.end_post.setText(postObject.getNoi_giao());

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    statusInterfaceRecyclerView.onItemClick(position);
                }
            });
        }
    }
}
