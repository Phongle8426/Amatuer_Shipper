package com.example.AmateurShipper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AmateurShipper.Util.PostDiffUtilCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;
import static com.example.AmateurShipper.LoginActivity.IDUSER;
import static com.example.AmateurShipper.LoginActivity.MyPREFERENCES;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewAdapterClass> {
    //private RecyclerViewClickInterface recyclerViewClickInterface;
    List<PostObject> postList;
    Context mContext;
    int get_position;
    String IdUser;
    public MainActivity mainActivity;
    private OnPostListener mOnPostListener;
    SharedPreferences sharedpreferencesIdUser;
    FirebaseDatabase rootDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = rootDatabase.getReference();
    public PostAdapter(List<PostObject> postList, Context context, OnPostListener onPostListener) {
        this.postList = postList;
        mContext = context;
        this.mOnPostListener = onPostListener;

    }

    public void insertData(List<PostObject> insertList) {
        PostDiffUtilCallback postDiffUtilCallback = new PostDiffUtilCallback(postList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffUtilCallback);
        postList.clear();
        postList.addAll(insertList);
        diffResult.dispatchUpdatesTo(PostAdapter.this);
    }
    public void updateData(List<PostObject> newList) {
        PostDiffUtilCallback postDiffUtilCallback = new PostDiffUtilCallback(postList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffUtilCallback);
        postList.clear();
        postList.addAll(newList);
        diffResult.dispatchUpdatesTo(PostAdapter.this);
    }

    public void addItem(int position,PostObject addList ) {
        postList.add(position, addList);
        notifyItemInserted(position);
    }
    @NonNull
    @Override
    public ViewAdapterClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_1, parent, false);
        ViewAdapterClass viewAdapterClass = new ViewAdapterClass(view, mOnPostListener);
        return viewAdapterClass;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewAdapterClass holder, int position) {
        ViewAdapterClass viewAdapterClass = (ViewAdapterClass) holder;
        PostObject postObject = postList.get(position);
        get_position = position;
        viewAdapterClass.name_poster.setText(postObject.getTen_nguoi_gui());
        viewAdapterClass.time.setText(postObject.getThoi_gian());
        viewAdapterClass.start_post.setText(postObject.getNoi_nhan());
        viewAdapterClass.end_post.setText(postObject.getNoi_giao());
        viewAdapterClass.distance.setText(String.valueOf(postObject.getKm()));
        viewAdapterClass.fee.setText(String.valueOf(postObject.getPhi_giao()));
        viewAdapterClass.payment.setText(String.valueOf(postObject.getPhi_ung()));
        viewAdapterClass.note.setText(postObject.getGhi_chu());

        // viewAdapterClass.image_poster.setImageResource(postObject.imgage_poster);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
        holder.itemView.startAnimation(animation);
        //Toast.makeText(mContext, "position" + get_position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewAdapterClass extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_poster, time, start_post, end_post, distance, fee, payment, note;
        CircleImageView image_poster;
        Button get_order, attach_image;
        OnPostListener onPostListener;

        public ViewAdapterClass(@NonNull final View itemView, OnPostListener onPostListener) {
            super(itemView);
            sharedpreferencesIdUser = itemView.getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            mainActivity = (MainActivity) itemView.getContext();
            name_poster = itemView.findViewById(R.id.name_poster);
            time = itemView.findViewById(R.id.time_post);
            start_post = itemView.findViewById(R.id.txt_start_place);
            end_post = itemView.findViewById(R.id.txt_end_place);
            distance = itemView.findViewById(R.id.txt_distance);
            //  quantity = itemView.findViewById(R.id.txt_quantity);
            fee = itemView.findViewById(R.id.txt_fee);
            payment = itemView.findViewById(R.id.txt_payment);
            note = itemView.findViewById(R.id.txt_note);
            image_poster = itemView.findViewById(R.id.img_poster);
            get_order = itemView.findViewById(R.id.img_getOrder);
            // attach_image = itemView.findViewById(R.id.img_attachment_image);
            loadData();
            this.onPostListener = onPostListener;
            itemView.setOnClickListener(this);
            get_order.setOnClickListener(new View.OnClickListener() {
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
                    String id_post = postList.get(getAdapterPosition()).id_post;
                    PostObject postObject = new PostObject(ten_nguoi_gui, sdt_nguoi_gui, noi_nhan, noi_giao, sdt_nguoi_nhan, ten_nguoi_nhan, ghi_chu, thoi_gian, id_shop, phi_giao, phi_ung, km, id_post,"0");
                    databaseReference.child("received_order_status").child(IdUser).child(postObject.getId_post()).setValue(postObject);
                    databaseReference.child("newsfeed").child(postObject.getId_post()).setValue(null);
                    postList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    mainActivity.setCountOrder(mainActivity.getmCountOrder()+1);

                    databaseReference.child("Transaction").child(postObject.getId_post()).child("id_shipper").setValue(IdUser);
                    databaseReference.child("OrderStatus").child(postObject.getId_shop()).child(postObject.getId_post()).child("status").setValue("1");

                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface OnPostListener {
        void onPostClick(int position);
    }

    //Load ID User
    private void loadData() {
        IdUser = sharedpreferencesIdUser.getString(IDUSER, "");
    }

}
