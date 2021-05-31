package com.example.AmateurShipper.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;
import com.example.AmateurShipper.Model.PostObject;
import com.example.AmateurShipper.R;
import com.example.AmateurShipper.Util.formatAddress;
import com.example.AmateurShipper.Util.formatTimeStampToDate;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShippingOrderAdapter extends RecyclerView.Adapter<ShippingOrderAdapter.ViewAdapterClass>{

    List<PostObject> shippingList;
    Fragment mContext;
    FragmentManager fragmentManager;
    private statusInterfaceRecyclerView clickInterface;

    public ShippingOrderAdapter(List<PostObject> shippingList,Fragment re,FragmentManager fm,statusInterfaceRecyclerView clickInterface){
       this.shippingList = shippingList;
       this.mContext = re;
       this.fragmentManager = fm;
       this.clickInterface = clickInterface;
    }
    @NonNull
    @Override
    public ViewAdapterClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab_nhan, parent, false);
        ShippingOrderAdapter.ViewAdapterClass viewAdapterClass = new ShippingOrderAdapter.ViewAdapterClass(view);
        return viewAdapterClass;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewAdapterClass holder, int position) {
    ViewAdapterClass viewAdapterClass = (ViewAdapterClass) holder;
        PostObject postObject = shippingList.get(position);
        formatTimeStampToDate ts = new formatTimeStampToDate();
        formatAddress faddress = new formatAddress();
        viewAdapterClass.txt_time_stamp.setText(ts.convertTimeStamp(Long.parseLong(postObject.getThoi_gian())));
        viewAdapterClass.name_poster_tab_nhan.setText(postObject.getTen_nguoi_gui());
        viewAdapterClass.txt_start_place_tab_nhan.setText(faddress.formatAddress(postObject.getNoi_nhan()));
        viewAdapterClass.txt_end_place_tab_nhan.setText(faddress.formatAddress(postObject.getNoi_giao()));
        viewAdapterClass.label_dang_giao.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return shippingList.size();
    }


    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        TextView name_poster_tab_nhan, txt_start_place_tab_nhan, txt_end_place_tab_nhan,txt_time_stamp,label_dang_giao;
        public ViewAdapterClass(@NonNull final View itemView) {
            super(itemView);
            name_poster_tab_nhan = itemView.findViewById(R.id.name_poster_tab_nhan);
            txt_start_place_tab_nhan = itemView.findViewById(R.id.txt_start_place_tab_nhan);
            txt_end_place_tab_nhan = itemView.findViewById(R.id.txt_end_place_tab_nhan);
            txt_time_stamp = itemView.findViewById(R.id.tv_time);
            label_dang_giao = itemView.findViewById(R.id.label_dang_giao);

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
