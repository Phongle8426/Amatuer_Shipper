package com.example.AmateurShipper.Adapter;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;
import com.example.AmateurShipper.Model.PostObject;
import com.example.AmateurShipper.R;
import com.example.AmateurShipper.Util.PostDiffUtilCallback;
import com.example.AmateurShipper.Util.formatAddress;
import com.example.AmateurShipper.Util.formatTimeStampToDate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ReceivedOrderAdapter extends RecyclerView.Adapter<ReceivedOrderAdapter.ViewAdapterClass>{

    List<PostObject> postList;
    Fragment mContext;
    int get_position;
    FragmentManager fragmentManager;

    FirebaseDatabase rootDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = rootDatabase.getReference();
    public static final String MyPREFERENCES_IDPOST = "myf";
    public static final String IDPOST = "ID post";
    SharedPreferences sharedpreferences;
    private statusInterfaceRecyclerView clickInterface;


    public ReceivedOrderAdapter(List<PostObject> postList, Fragment re, FragmentManager fm,statusInterfaceRecyclerView clickInterface) {
        this.postList = postList;
        mContext = re;

        fragmentManager = fm;
        this.clickInterface = clickInterface;
    }

    public void insertData(List<PostObject> insertList) {
        PostDiffUtilCallback postDiffUtilCallback = new PostDiffUtilCallback(postList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffUtilCallback);
        postList.clear();
        postList.addAll(insertList);
        diffResult.dispatchUpdatesTo(ReceivedOrderAdapter.this);
    }

    public void addItem(int position,PostObject addList ) {
            postList.add(position, addList);
            notifyItemInserted(position);
    }

    @NonNull
    @Override
    public ReceivedOrderAdapter.ViewAdapterClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab_nhan, parent, false);
        ReceivedOrderAdapter.ViewAdapterClass viewAdapterClass = new ReceivedOrderAdapter.ViewAdapterClass(view);
        return viewAdapterClass;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewAdapterClass holder, int position) {
        ViewAdapterClass viewAdapterClass = (ViewAdapterClass) holder;
        PostObject postObject = postList.get(position);
        get_position = position;
        formatTimeStampToDate ts = new formatTimeStampToDate();
        formatAddress faddress = new formatAddress();
        viewAdapterClass.name_poster_tab_nhan.setText(postObject.getTen_nguoi_gui());
        viewAdapterClass.txt_time_stamp.setText(ts.convertTimeStamp(Long.parseLong(postObject.getThoi_gian())));
        viewAdapterClass.txt_start_place_tab_nhan.setText(faddress.formatAddress(postObject.getNoi_nhan()));
        viewAdapterClass.txt_end_place_tab_nhan.setText(faddress.formatAddress(postObject.getNoi_giao()));
        viewAdapterClass.label_nhan_dơn.setVisibility(View.VISIBLE);
//        Long currentTime = System.currentTimeMillis() / 1000;
//        long time = Long.parseLong(postObject.getThoi_gian())-currentTime;
//        if (time>2){
//            viewAdapterClass.timer = new CountDownTimer(time, 1000) {
//                @Override
//                public void onTick(long l) {
//                }
//
//                @Override
//                public void onFinish() {
//                    viewAdapterClass.img_timer.setVisibility(View.VISIBLE);
//                }
//            }.start();
//        }else viewAdapterClass.img_timer.setVisibility(View.VISIBLE);


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewAdapterClass extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_poster_tab_nhan,txt_start_place_tab_nhan, txt_end_place_tab_nhan,txt_time_stamp,label_nhan_dơn;
        ImageView img_timer;
        CountDownTimer timer;

        public ViewAdapterClass(@NonNull final View itemView) {
            super(itemView);
            name_poster_tab_nhan = itemView.findViewById(R.id.name_poster_tab_nhan);
            txt_start_place_tab_nhan = itemView.findViewById(R.id.txt_start_place_tab_nhan);
            txt_end_place_tab_nhan = itemView.findViewById(R.id.txt_end_place_tab_nhan);
            txt_time_stamp = itemView.findViewById(R.id.tv_time);
            img_timer= itemView.findViewById(R.id.img_timer);
            label_nhan_dơn = itemView.findViewById(R.id.label_da_nhan);


            img_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext.getContext(), "Chú ý!!"+"/nĐơn này đã quá thời gian dự kiến", Toast.LENGTH_SHORT).show();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    clickInterface.onItemClick(position);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void saveIdChatRoom(String idPost){
        final String[] id_chat_room = new String[1];
        databaseReference.child("Transaction").child(idPost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    id_chat_room[0] = snapshot.child("id_roomchat").getValue(String.class);
                    // databaseReference.child("Transaction").child(idPost).removeEventListener(this);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(ReceivedOrderAdapter.IDPOST, id_chat_room[0]);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public interface OnReceivedOderListener {
        void onReceivedItem(int position);
    }

}