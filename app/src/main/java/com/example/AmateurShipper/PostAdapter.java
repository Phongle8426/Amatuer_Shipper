package com.example.AmateurShipper;

import android.content.Context;
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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewAdapterClass> {

    //private RecyclerViewClickInterface recyclerViewClickInterface;
    List<PostObject> postList;
    Context mContext;

    public PostAdapter(List<PostObject> postList,Context context){
        this.postList = postList;
        mContext = context;
    }

    public void insertData(List<PostObject> insertList){
        PostDiffUtilCallback postDiffUtilCallback = new PostDiffUtilCallback(postList,insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffUtilCallback);
        postList.clear();
        postList.addAll(insertList);
        diffResult.dispatchUpdatesTo(PostAdapter.this);
    }

    @NonNull
    @Override
    public ViewAdapterClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_1,parent,false);
        ViewAdapterClass viewAdapterClass = new ViewAdapterClass(view);
        return viewAdapterClass;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewAdapterClass holder, int position) {
        ViewAdapterClass viewAdapterClass = (ViewAdapterClass)holder;
        PostObject postObject =postList.get(position);
        viewAdapterClass.name_poster.setText(postObject.getTen_nguoi_gui());
        viewAdapterClass.time.setText(postObject.getThoi_gian());
        viewAdapterClass.start_post.setText(postObject.getNoi_nhan());
        viewAdapterClass.end_post.setText(postObject.getNoi_giao());
        viewAdapterClass.distance.setText(String.valueOf(postObject.getKm()));
        viewAdapterClass.fee.setText(String.valueOf(postObject.getPhi_giao()));
        viewAdapterClass.payment.setText(String.valueOf(postObject.getPhi_ung()));
        viewAdapterClass.note.setText(postObject.getGhi_chu());
       // viewAdapterClass.image_poster.setImageResource(postObject.imgage_poster);
        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_right);
        holder.itemView.startAnimation(animation);
        Toast.makeText(mContext, ""+viewAdapterClass.name_poster.getText().toString(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewAdapterClass extends RecyclerView.ViewHolder{
        TextView name_poster,time,start_post,end_post,distance,fee,payment,note;
        CircleImageView image_poster;
        Button get_order,attach_image;
        public ViewAdapterClass(@NonNull final View itemView) {
            super(itemView);
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

            get_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "tina", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
