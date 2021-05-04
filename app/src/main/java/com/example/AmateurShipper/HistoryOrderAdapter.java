package com.example.AmateurShipper;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;
import com.example.AmateurShipper.Util.formatAddress;
import com.example.AmateurShipper.Util.formatTimeStampToDate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.ViewAdapterClass> {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    List<PostObject> historyList;
    Fragment mContext;
    FragmentManager fragmentManager;
    private statusInterfaceRecyclerView clickInterface;

    public HistoryOrderAdapter(List<PostObject> historyList, Fragment mContext, FragmentManager fragmentManager, statusInterfaceRecyclerView clickInterface) {
        this.historyList = historyList;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
        this.clickInterface = clickInterface;
    }

    @NonNull
    @Override
    public HistoryOrderAdapter.ViewAdapterClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab_nhan, parent, false);
        HistoryOrderAdapter.ViewAdapterClass viewAdapterClass = new HistoryOrderAdapter.ViewAdapterClass(view);
        return viewAdapterClass;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOrderAdapter.ViewAdapterClass holder, int position) {
        HistoryOrderAdapter.ViewAdapterClass viewAdapterClass = (HistoryOrderAdapter.ViewAdapterClass) holder;
        PostObject postObject = historyList.get(position);
        formatTimeStampToDate ts = new formatTimeStampToDate();
        formatAddress faddress = new formatAddress();
        viewAdapterClass.txt_time_stamp.setText(ts.convertTimeStamp(Long.parseLong(postObject.getThoi_gian())));
        viewAdapterClass.name_poster_tab_nhan.setText(postObject.getTen_nguoi_gui());
        viewAdapterClass.txt_start_place_tab_nhan.setText(faddress.formatAddress(postObject.getNoi_nhan()));
        viewAdapterClass.txt_end_place_tab_nhan.setText(faddress.formatAddress(postObject.getNoi_giao()));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        TextView name_poster_tab_nhan, txt_start_place_tab_nhan, txt_end_place_tab_nhan,txt_time_stamp;
        public ViewAdapterClass(@NonNull View itemView) {
            super(itemView);
            name_poster_tab_nhan = itemView.findViewById(R.id.name_poster_tab_nhan);
            txt_start_place_tab_nhan = itemView.findViewById(R.id.txt_start_place_tab_nhan);
            txt_end_place_tab_nhan = itemView.findViewById(R.id.txt_end_place_tab_nhan);
            txt_time_stamp = itemView.findViewById(R.id.tv_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    int position = getLayoutPosition();
                    clickInterface.onItemClick(position);
                }

            });
        }
    }

}