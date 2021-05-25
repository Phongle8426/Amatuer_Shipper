package com.example.AmateurShipper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;
import com.example.AmateurShipper.Util.PostDiffUtilCallback;
import com.example.AmateurShipper.Util.formatAddress;
import com.example.AmateurShipper.Util.formatTimeStampToDate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;
import static com.example.AmateurShipper.ReceivedOrderAdapter.MyPREFERENCES_IDPOST;

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
    }

    @Override
    public int getItemCount() {
        return shippingList.size();
    }


    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        TextView name_poster_tab_nhan, txt_start_place_tab_nhan, txt_end_place_tab_nhan,txt_time_stamp;
        public ViewAdapterClass(@NonNull final View itemView) {
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
