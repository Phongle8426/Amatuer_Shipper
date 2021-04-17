package com.example.AmateurShipper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AmateurShipper.Util.PostDiffUtilCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.AmateurShipper.ReceivedOrderAdapter.MyPREFERENCES_IDPOST;

public class ShippingOrderAdapter extends RecyclerView.Adapter<ShippingOrderAdapter.ViewAdapterClass>{

    int get_position;
    SharedPreferences sharedpreferences;
    FirebaseDatabase rootDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = rootDatabase.getReference();
    List<PostObject> shippingList;
    Fragment mContext;
    FragmentManager fragmentManager;
    String sdt_nguoi_nhan_hang,sdt_shop;

    public ShippingOrderAdapter(List<PostObject> shippingList,Fragment re,FragmentManager fm){
       this.shippingList = shippingList;
       this.mContext = re;
       this.fragmentManager = fm;
    }

    public void insertData(List<PostObject> insertList) {
        PostDiffUtilCallback postDiffUtilCallback = new PostDiffUtilCallback(shippingList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffUtilCallback);
        shippingList.clear();
        shippingList.addAll(insertList);
        diffResult.dispatchUpdatesTo(ShippingOrderAdapter.this);
    }

    public void addItem(int position,PostObject addList ) {
//        if (shippingList.size()>0){
//            if(!addList.id_post.equals(shippingList.get(0).id_post)){
//                shippingList.add(position, addList);
//                notifyItemInserted(position);
//            }
//        }else{
            shippingList.add(position, addList);
            notifyItemInserted(position);
       // }
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
        viewAdapterClass.name_poster_tab_nhan.setText(postObject.getTen_nguoi_gui());
        viewAdapterClass.txt_start_place_tab_nhan.setText(postObject.getNoi_nhan());
        viewAdapterClass.txt_end_place_tab_nhan.setText(postObject.getNoi_giao());
    }

    @Override
    public int getItemCount() {
        return shippingList.size();
    }



    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        TextView name_poster_tab_nhan, txt_start_place_tab_nhan, txt_end_place_tab_nhan,tv_shopname;
        private static final int REQUEST_CALL = 1;
        private TextView tv_sdt_nguoi_nhan;
        private Button btn_sdt_nguoi_nhan, btn_shop, btn_messages;
        LinearLayout linearLayout, containerChat;
        ImageButton close_btn;
        public ViewAdapterClass(@NonNull final View itemView) {
            super(itemView);
            name_poster_tab_nhan = itemView.findViewById(R.id.name_poster_tab_nhan);
            txt_start_place_tab_nhan = itemView.findViewById(R.id.txt_start_place_tab_nhan);
            txt_end_place_tab_nhan = itemView.findViewById(R.id.txt_end_place_tab_nhan);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    String ten_nguoi_gui = shippingList.get(getAdapterPosition()).ten_nguoi_gui;
                    String sdt_nguoi_gui = shippingList.get(getAdapterPosition()).sdt_nguoi_gui;
                    String noi_nhan = shippingList.get(getAdapterPosition()).noi_nhan;
                    String noi_giao = shippingList.get(getAdapterPosition()).noi_giao;
                    String sdt_nguoi_nhan = shippingList.get(getAdapterPosition()).sdt_nguoi_nhan;
                    String ten_nguoi_nhan = shippingList.get(getAdapterPosition()).ten_nguoi_nhan;
                    String ghi_chu = shippingList.get(getAdapterPosition()).ghi_chu;
                    String thoi_gian = shippingList.get(getAdapterPosition()).thoi_gian;
                    String id_shop = shippingList.get(getAdapterPosition()).id_shop;
                    String phi_giao = shippingList.get(getAdapterPosition()).phi_giao;
                    String phi_ung = shippingList.get(getAdapterPosition()).phi_ung;
                    String km = shippingList.get(getAdapterPosition()).km;
                    String id_post = shippingList.get(getAdapterPosition()).id_post;

                    sharedpreferences = mContext.getActivity().getSharedPreferences(MyPREFERENCES_IDPOST, Context.MODE_PRIVATE);
                    saveIdChatRoom(id_post);
                   // Toast.makeText(mContext.getActivity(), "ten nguoi gui"+ten_nguoi_gui, Toast.LENGTH_SHORT).show();
                    final DialogPlus dialogPlus = DialogPlus.newDialog(itemView.getContext())
                            .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                            .setGravity(Gravity.CENTER)
                            .create();
                    final View myview_dia = dialogPlus.getHolderView();
                    //Khai báo
                    linearLayout = (LinearLayout) myview_dia.findViewById(R.id.dialog_received_order);
                    //TextView tng = (TextView) myview_dia.findViewById(R.id.editTextTenNguoiGui);
                    TextView sdtnguoigui = (TextView) myview_dia.findViewById(R.id.editTextSoDTNguoiGui);
                    TextView noinhan = (TextView) myview_dia.findViewById(R.id.editTextTextDiemdi);
                    TextView noigiao = (TextView) myview_dia.findViewById(R.id.editTextTextDiemden);
                    TextView sdtnguoinhan = (TextView) myview_dia.findViewById(R.id.tv_sdt_nguoi_nhan);
                    TextView tennguoinhan = (TextView) myview_dia.findViewById(R.id.tv_ten_nguoi_nhan);
                    TextView ghichu = (TextView) myview_dia.findViewById(R.id.editTextTextGhiChu);
                    TextView thoigian = (TextView) myview_dia.findViewById(R.id.editTextTextPhut);
                    TextView phigiao = (TextView) myview_dia.findViewById(R.id.editTextTextTienPhi);
                    TextView phiung = (TextView) myview_dia.findViewById(R.id.editTextTextTienUng);
                    TextView sokm = (TextView) myview_dia.findViewById(R.id.editTextTextKm);
                    containerChat = (LinearLayout) myview_dia.findViewById(R.id.container_chat) ;
                    tv_shopname = myview_dia.findViewById(R.id.username);
                    close_btn = myview_dia.findViewById(R.id.close_chat);

                    //Set data vao view
                    sdtnguoigui.setText(sdt_nguoi_gui);
                    noinhan.setText(noi_nhan);
                    noigiao.setText(noi_giao);
                    sdtnguoinhan.setText(sdt_nguoi_nhan);
                    tennguoinhan.setText(ten_nguoi_nhan);
                    ghichu.setText(ghi_chu);
                    thoigian.setText(thoi_gian);
                    phigiao.setText(phi_giao);
                    phiung.setText(phi_ung);
                    sokm.setText(km);
                    tv_shopname.setText(ten_nguoi_gui);
                    dialogPlus.show();

                    //lay sdt shop va khach hang
                    sdt_nguoi_nhan_hang = sdtnguoinhan.getText().toString();
                    sdt_shop = sdtnguoigui.getText().toString();
                    btn_sdt_nguoi_nhan = (Button) myview_dia.findViewById(R.id.btn_customer_phone_number);
                    //tv_sdt_nguoi_nhan = (TextView)myview_dia.findViewById(R.id.tv_sdt_nguoi_nhan);
                    btn_shop = (Button) myview_dia.findViewById(R.id.btn_shop);
                    btn_messages = (Button) myview_dia.findViewById(R.id.btn_massage);
                    final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frag_container_1);
                    btn_sdt_nguoi_nhan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  call_customer();
                        }
                    });
                    btn_shop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // call_shop();
                        }
                    });
                    btn_messages.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            linearLayout.setVisibility(View.GONE);
                            // dialogPlus.dismiss();
                            ChatFragment chatFragment = new ChatFragment();
                            fragmentManager = mContext.getActivity().getSupportFragmentManager();
                            mContext.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container_1, chatFragment).commit();
                        }
                    });
                    close_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogPlus.dismiss();
                        }
                    });
                }
            });
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
}
