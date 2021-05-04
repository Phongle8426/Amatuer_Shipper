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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;
import com.example.AmateurShipper.Util.PostDiffUtilCallback;
import com.example.AmateurShipper.Util.formatAddress;
import com.example.AmateurShipper.Util.formatTimeStampToDate;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;


public class ReceivedOrderAdapter extends RecyclerView.Adapter<ReceivedOrderAdapter.ViewAdapterClass> implements ActivityCompat.OnRequestPermissionsResultCallback {

    List<PostObject> postList;
    Fragment mContext;
    int get_position;
    FragmentManager fragmentManager;
    private OnReceivedOderListener mOnReceivedOderListener;
    FirebaseDatabase rootDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = rootDatabase.getReference();
    public static final String MyPREFERENCES_IDPOST = "myf";
    public static final String IDPOST = "ID post";
    SharedPreferences sharedpreferences;
    private statusInterfaceRecyclerView clickInterface;

    public ReceivedOrderAdapter(List<PostObject> postList, Fragment re,
                                OnReceivedOderListener onReceivedOderListener, FragmentManager fm,statusInterfaceRecyclerView clickInterface) {
        this.postList = postList;
        mContext = re;
        this.mOnReceivedOderListener = onReceivedOderListener;
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

    public void updateData(List<PostObject> newList) {
        PostDiffUtilCallback postDiffUtilCallback = new PostDiffUtilCallback(postList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffUtilCallback);
        postList.clear();
        postList.addAll(newList);
        diffResult.dispatchUpdatesTo(ReceivedOrderAdapter.this);
    }
    public void addItem(int position,PostObject addList ) {
            postList.add(position, addList);
            notifyItemInserted(position);
           // id_postList.add(addList.id_post);
    }

    @NonNull
    @Override
    public ReceivedOrderAdapter.ViewAdapterClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab_nhan, parent, false);
        ReceivedOrderAdapter.ViewAdapterClass viewAdapterClass = new ReceivedOrderAdapter.ViewAdapterClass(view, mOnReceivedOderListener);
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
        Animation animation = AnimationUtils.loadAnimation(mContext.getActivity(), R.anim.slide_in_right);
        holder.itemView.startAnimation(animation);


        //Toast.makeText(mContext, "ten guoi guirw" + viewAdapterClass.name_poster_tab_nhan.getText().toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ViewAdapterClass.REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call_customer();
                call_shop();
            } else {
                Toast.makeText(mContext.getActivity(), "permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void call_shop() {
        String number_shop = "0" + sdt_shop;
        if (number_shop.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(mContext.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(mContext.getActivity(), new String[]{Manifest.permission.CALL_PHONE}, ViewAdapterClass.REQUEST_CALL);
            else {
                String dial = "tel: " + number_shop;
                mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    private void call_customer() {
        String number_customer = "0" + sdt_nguoi_nhan_hang;
        if (number_customer.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(mContext.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(mContext.getActivity(), new String[]{Manifest.permission.CALL_PHONE}, ViewAdapterClass.REQUEST_CALL);
            else {
                String dial = "tel: " + number_customer;
                mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }
    String sdt_nguoi_nhan_hang;
    String sdt_shop;
    public class ViewAdapterClass extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_poster_tab_nhan,txt_start_place_tab_nhan, txt_end_place_tab_nhan,txt_time_stamp;
        CircleImageView image_poster;
        private static final int REQUEST_CALL = 1;

        public ViewAdapterClass(@NonNull final View itemView, ReceivedOrderAdapter.OnReceivedOderListener onReceivedOderListener) {
            super(itemView);
            name_poster_tab_nhan = itemView.findViewById(R.id.name_poster_tab_nhan);
            txt_start_place_tab_nhan = itemView.findViewById(R.id.txt_start_place_tab_nhan);
            txt_end_place_tab_nhan = itemView.findViewById(R.id.txt_end_place_tab_nhan);
            txt_time_stamp = itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    clickInterface.onItemClick(position);

                }
            });
//            this.onReceivedOderListener = onReceivedOderListener;
//            itemView.setOnClickListener(this);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View view) {
//                    String ten_nguoi_gui = postList.get(getAdapterPosition()).ten_nguoi_gui;
//                    String sdt_nguoi_gui = postList.get(getAdapterPosition()).sdt_nguoi_gui;
//                    String noi_nhan = postList.get(getAdapterPosition()).noi_nhan;
//                    String noi_giao = postList.get(getAdapterPosition()).noi_giao;
//                    String sdt_nguoi_nhan = postList.get(getAdapterPosition()).sdt_nguoi_nhan;
//                    String ten_nguoi_nhan = postList.get(getAdapterPosition()).ten_nguoi_nhan;
//                    String ghi_chu = postList.get(getAdapterPosition()).ghi_chu;
//                    String thoi_gian = postList.get(getAdapterPosition()).thoi_gian;
//                    String id_shop = postList.get(getAdapterPosition()).id_shop;
//                    String phi_giao = postList.get(getAdapterPosition()).phi_giao;
//                    String phi_ung = postList.get(getAdapterPosition()).phi_ung;
//                    String km = postList.get(getAdapterPosition()).km;
//                    String id_post = postList.get(getAdapterPosition()).id_post;
//
//                    sharedpreferences = mContext.getActivity().getSharedPreferences(MyPREFERENCES_IDPOST, Context.MODE_PRIVATE);
//                    saveIdChatRoom(id_post);
//                    Toast.makeText(mContext.getActivity(), "ten nguoi gui"+ten_nguoi_gui, Toast.LENGTH_SHORT).show();
//                    final DialogPlus dialogPlus = DialogPlus.newDialog(itemView.getContext())
//                            .setContentHolder(new ViewHolder(R.layout.dialogcontent))
//                            .setGravity(Gravity.CENTER)
//                            .create();
//                    final View myview_dia = dialogPlus.getHolderView();
//                    //Khai báo
//                    linearLayout = (LinearLayout) myview_dia.findViewById(R.id.dialog_received_order);
//                    //TextView tng = (TextView) myview_dia.findViewById(R.id.editTextTenNguoiGui);
//                    TextView sdtnguoigui = (TextView) myview_dia.findViewById(R.id.editTextSoDTNguoiGui);
//                    TextView noinhan = (TextView) myview_dia.findViewById(R.id.editTextTextDiemdi);
//                    TextView noigiao = (TextView) myview_dia.findViewById(R.id.editTextTextDiemden);
//                    TextView sdtnguoinhan = (TextView) myview_dia.findViewById(R.id.tv_sdt_nguoi_nhan);
//                    TextView tennguoinhan = (TextView) myview_dia.findViewById(R.id.tv_ten_nguoi_nhan);
//                    TextView ghichu = (TextView) myview_dia.findViewById(R.id.editTextTextGhiChu);
//                    TextView thoigian = (TextView) myview_dia.findViewById(R.id.editTextTextPhut);
//                    TextView phigiao = (TextView) myview_dia.findViewById(R.id.editTextTextTienPhi);
//                    TextView phiung = (TextView) myview_dia.findViewById(R.id.editTextTextTienUng);
//                    TextView sokm = (TextView) myview_dia.findViewById(R.id.editTextTextKm);
//                    containerChat = (LinearLayout) myview_dia.findViewById(R.id.container_chat) ;
//                    tv_shopname = myview_dia.findViewById(R.id.username);
//                    close_btn = myview_dia.findViewById(R.id.close_chat);
//
//                    //Set data vao view
//                    sdtnguoigui.setText(sdt_nguoi_gui);
//                    noinhan.setText(noi_nhan);
//                    noigiao.setText(noi_giao);
//                    sdtnguoinhan.setText(sdt_nguoi_nhan);
//                    tennguoinhan.setText(ten_nguoi_nhan);
//                    ghichu.setText(ghi_chu);
//                    thoigian.setText(thoi_gian);
//                    phigiao.setText(phi_giao);
//                    phiung.setText(phi_ung);
//                    sokm.setText(km);
//                    tv_shopname.setText(ten_nguoi_gui);
//                    dialogPlus.show();
//
//                    //lay sdt shop va khach hang
//                    sdt_nguoi_nhan_hang = sdtnguoinhan.getText().toString();
//                    sdt_shop = sdtnguoigui.getText().toString();
//                    btn_sdt_nguoi_nhan = (Button) myview_dia.findViewById(R.id.btn_customer_phone_number);
//                    //tv_sdt_nguoi_nhan = (TextView)myview_dia.findViewById(R.id.tv_sdt_nguoi_nhan);
//                    btn_shop = (Button) myview_dia.findViewById(R.id.btn_shop);
//                    btn_messages = (Button) myview_dia.findViewById(R.id.btn_massage);
//                    final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frag_container_1);
//                    btn_sdt_nguoi_nhan.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            call_customer();
//                        }
//                    });
//                    btn_shop.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            call_shop();
//                        }
//                    });
//                    btn_messages.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                           linearLayout.setVisibility(View.GONE);
//                           // dialogPlus.dismiss();
//                            ChatFragment chatFragment = new ChatFragment();
//                            fragmentManager = mContext.getActivity().getSupportFragmentManager();
//                            mContext.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container_1, chatFragment).commit();
//                        }
//                    });
//                    close_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialogPlus.dismiss();
//                        }
//                    });
//                }
//            });
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