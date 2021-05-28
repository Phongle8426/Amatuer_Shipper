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

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewAdapterClass>{

    List<PostObject> mapList;
    Fragment mContext;
    FragmentManager fragmentManager;
    private statusInterfaceRecyclerView clickInterface;

    public MapAdapter(List<PostObject> mapList,Fragment re,FragmentManager fm,statusInterfaceRecyclerView clickInterface){
        this.mapList = mapList;
        this.mContext = re;
        this.fragmentManager = fm;
        this.clickInterface = clickInterface;
    }
    @NonNull
    @Override
    public ViewAdapterClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_map, parent, false);
        MapAdapter.ViewAdapterClass viewAdapterClass = new MapAdapter.ViewAdapterClass(view);
        return viewAdapterClass;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewAdapterClass holder, int position) {
        ViewAdapterClass viewAdapterClass = (ViewAdapterClass) holder;
        PostObject postObject = mapList.get(position);
        formatTimeStampToDate ts = new formatTimeStampToDate();
        formatAddress faddress = new formatAddress();
        viewAdapterClass.txt_start_place_tab_nhan.setText(faddress.formatAddress(postObject.getNoi_nhan()));
        viewAdapterClass.txt_end_place_tab_nhan.setText(faddress.formatAddress(postObject.getNoi_giao()));

    }
    @Override
    public int getItemCount() {
        return mapList.size();
    }
    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        TextView txt_start_place_tab_nhan, txt_end_place_tab_nhan;
        public ViewAdapterClass(@NonNull final View itemView) {
            super(itemView);

            txt_start_place_tab_nhan = itemView.findViewById(R.id.txt_start_place_tab_nhan);
            txt_end_place_tab_nhan = itemView.findViewById(R.id.txt_end_place_tab_nhan);


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
