package com.example.AmateurShipper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter {

    //private RecyclerViewClickInterface recyclerViewClickInterface;
    List<PostObject> postList;
    Context mContext;

    public PostAdapter(List<PostObject> postList,Context context){
        this.postList = postList;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_1,parent,false);
        ViewAdapterClass viewAdapterClass = new ViewAdapterClass(view);
        return viewAdapterClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewAdapterClass viewAdapterClass = (ViewAdapterClass)holder;
        PostObject postObject =postList.get(position);
        viewAdapterClass.name_poster.setText(postObject.getName_poster());
        viewAdapterClass.time.setText(postObject.getTime());
        viewAdapterClass.start_post.setText(postObject.getStart_post());
        viewAdapterClass.end_post.setText(postObject.getEnd_post());
        viewAdapterClass.distance.setText(postObject.getDistance());
    //    viewAdapterClass.quantity.setText(String.valueOf(postObject.getQuantity_order()));
        viewAdapterClass.fee.setText(String.valueOf(postObject.getFee()));
        viewAdapterClass.payment.setText(String.valueOf(postObject.getPayment()));
        viewAdapterClass.note.setText(postObject.getNote());
        viewAdapterClass.image_poster.setImageResource(postObject.imgage_poster);

        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_right);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewAdapterClass extends RecyclerView.ViewHolder{
        TextView name_poster,time,start_post,end_post,distance,quantity,fee,payment,note;
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
                    Toast.makeText(view.getContext(), "tin an dau buoi", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
