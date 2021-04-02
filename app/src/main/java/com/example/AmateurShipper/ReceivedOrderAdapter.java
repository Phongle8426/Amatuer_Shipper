package com.example.AmateurShipper;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AmateurShipper.Util.PostDiffUtilCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReceivedOrderAdapter extends RecyclerView.Adapter<ReceivedOrderAdapter.ViewAdapterClass> {


    List<PostObject> postList;
    Context mContext;
    int get_position;
    private OnReceivedOderListener mOnReceivedOderListener;
    FirebaseDatabase rootDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = rootDatabase.getReference();
    public ReceivedOrderAdapter(List<PostObject> postList, Context context, OnReceivedOderListener onReceivedOderListener) {
        this.postList = postList;
        mContext = context;
        this.mOnReceivedOderListener = onReceivedOderListener;
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
        viewAdapterClass.name_poster_tab_nhan.setText(postObject.getTen_nguoi_gui());
      //  viewAdapterClass.time.setText(postObject.getThoi_gian());
        viewAdapterClass.txt_start_place_tab_nhan.setText(postObject.getNoi_nhan());
        viewAdapterClass.txt_end_place_tab_nhan.setText(postObject.getNoi_giao());
     //   viewAdapterClass.distance.setText(String.valueOf(postObject.getKm()));
     //  viewAdapterClass.fee.setText(String.valueOf(postObject.getPhi_giao()));
     //   viewAdapterClass.payment.setText(String.valueOf(postObject.getPhi_ung()));
      //  viewAdapterClass.note.setText(postObject.getGhi_chu());
        // viewAdapterClass.image_poster.setImageResource(postObject.imgage_poster);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
        holder.itemView.startAnimation(animation);
        //Toast.makeText(mContext, "ten guoi guirw" + viewAdapterClass.name_poster_tab_nhan.getText().toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewAdapterClass extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_poster_tab_nhan, time, txt_start_place_tab_nhan, txt_end_place_tab_nhan, distance, fee, payment, note;
        CircleImageView image_poster;

       //PostAdapter.OnPostListener onPostListener;
        ReceivedOrderAdapter.OnReceivedOderListener onReceivedOderListener;
        public ViewAdapterClass(@NonNull final View itemView, ReceivedOrderAdapter.OnReceivedOderListener onReceivedOderListener) {
            super(itemView);
            name_poster_tab_nhan = itemView.findViewById(R.id.name_poster_tab_nhan);
           // time = itemView.findViewById(R.id.time_post);
            txt_start_place_tab_nhan = itemView.findViewById(R.id.txt_start_place_tab_nhan);
            txt_end_place_tab_nhan = itemView.findViewById(R.id.txt_end_place_tab_nhan);
           // distance = itemView.findViewById(R.id.txt_distance);
            //  quantity = itemView.findViewById(R.id.txt_quantity);
          //  fee = itemView.findViewById(R.id.txt_fee);
          //  payment = itemView.findViewById(R.id.txt_payment);
          //  note = itemView.findViewById(R.id.txt_note);
          //  image_poster = itemView.findViewById(R.id.img_poster);
            //get_order = itemView.findViewById(R.id.img_getOrder);
            // attach_image = itemView.findViewById(R.id.img_attachment_image);
          //  this.onPostListener = onPostListener;
            this.onReceivedOderListener = onReceivedOderListener;
            itemView.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ten_nguoi_gui = postList.get(getAdapterPosition()).ten_nguoi_gui;
                    String sdt_nguoi_gui = postList.get(getAdapterPosition()).getSdt_nguoi_gui();
                    String noi_nhan = postList.get(getAdapterPosition()).noi_nhan;
                    String noi_giao = postList.get(getAdapterPosition()).noi_giao;
                    String sdt_nguoi_nhan = postList.get(getAdapterPosition()).sdt_nguoi_nhan;
                    String ten_nguoi_nhan = postList.get(getAdapterPosition()).ten_nguoi_nhan;
                    String ghi_chu = postList.get(getAdapterPosition()).ghi_chu;
                    String thoi_gian = postList.get(getAdapterPosition()).thoi_gian;
                    String id_shop = postList.get(getAdapterPosition()).id_shop;
                    String phi_giao = postList.get(getAdapterPosition()).phi_giao;
                    String phi_ung = postList.get(getAdapterPosition()).phi_ung;
                    String km = postList.get(getAdapterPosition()).km;

                    final DialogPlus dialogPlus=DialogPlus.newDialog(itemView.getContext())
                            .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                            .setGravity(Gravity.CENTER)
                            .create();
                    View myview_dia = dialogPlus.getHolderView();
                    TextView tng =  (TextView)myview_dia.findViewById(R.id.editTextTenNguoiGui);
                    TextView sdtnguoigui =(TextView)myview_dia.findViewById(R.id.editTextSdtNguoiGui);
                    TextView noinhan =(TextView)myview_dia.findViewById(R.id.editTextTextDiemdi);
                    TextView noigiao =(TextView)myview_dia.findViewById(R.id.editTextTextDiemden);
                    TextView sdtnguoinhan =(TextView)myview_dia.findViewById(R.id.editTextTextSDTNGuoiNhan    );
                    TextView tennguoinhan =(TextView)myview_dia.findViewById(R.id.editTextTextTenNGuoiNhan);
                    TextView ghichu =(TextView)myview_dia.findViewById(R.id.editTextTextGhiChu);
                    TextView thoigian =(TextView)myview_dia.findViewById(R.id.editTextTextPhut);
                    //TextView idshop =(TextView)myview_dia.findViewById(R.id.editTextIdShop);
                    TextView phigiao =(TextView)myview_dia.findViewById(R.id.editTextTextTienPhi);
                    TextView phiung=(TextView)myview_dia.findViewById(R.id.editTextTextTienUng);
                    TextView sokm =(TextView)myview_dia.findViewById(R.id.editTextTextKm);

                    tng.setText(ten_nguoi_gui);
                    sdtnguoigui.setText(sdt_nguoi_gui);
                    noinhan.setText(noi_nhan);
                    noigiao.setText(noi_giao);
                    sdtnguoinhan.setText(sdt_nguoi_nhan);

                    tennguoinhan.setText(ten_nguoi_nhan);
                    ghichu.setText(ghi_chu);
                    thoigian.setText(thoi_gian);
                   // idshop.setText(id_shop);
                    phigiao.setText(phi_giao);
                    phiung.setText(phi_ung);
                    sokm.setText(km);
                    dialogPlus.show();
//                    PostObject postObject = new PostObject(ten_nguoi_gui, sdt_nguoi_gui, noi_nhan, noi_giao, sdt_nguoi_nhan, ten_nguoi_nhan, ghi_chu, thoi_gian, id_shop, phi_giao, phi_ung, km, "keyvalue2");
//                    databaseReference.child("received_order_status").child(postObject.getKeypush()).setValue(postObject);
//                    databaseReference.child("nsf").setValue(null);
//                    postList.remove(get_position);
//                    notifyItemRemoved(get_position);

                }
            });
        }
        @Override
        public void onClick(View v) {

           // .onPostClick(getAdapterPosition());
        }
    }

    public interface OnReceivedOderListener {
        void onReceivedItem(int position);
    }
}