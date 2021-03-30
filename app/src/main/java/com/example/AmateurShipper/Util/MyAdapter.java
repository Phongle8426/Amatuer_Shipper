//package com.example.AmateurShipper.Util;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.RecyclerView;
//
//
//import com.example.AmateurShipper.R;
//
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//
//    List<String> dataSource;
//
//    public MyAdapter(List<String> dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    public void insertData(List<String> insertList) {
//        //This function will add new data to Recyclerview
//        MyDiffUtilCallBack diffUtilCallBack = new MyDiffUtilCallBack(dataSource, insertList);
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallBack);
//        dataSource.addAll(insertList);
//        diffResult.dispatchUpdatesTo(this);
//    }
//
//    public void updateData(List<String> newList) {
//        //This function will clear old  data  and add new
//        MyDiffUtilCallBack diffUtilCallBack = new MyDiffUtilCallBack(dataSource, newList);
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallBack);
//        dataSource.clear();
//        dataSource.addAll(newList);
//        diffResult.dispatchUpdatesTo(this);
//    }
//
//    @NonNull
//    @Override
//    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_1, parent, false);
//        return new MyViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
//        holder.my_text_view.setText(dataSource.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return dataSource.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView my_text_view, name_post, time_post, distance, txt_start_place,txt_end_place, txt_fee, txt_payment ;
//        Button btn_getOrder;
//        CircleImageView imagePoster;
//
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            my_text_view = (TextView) itemView.findViewById(R.id.);
//        }
//    }
//}
