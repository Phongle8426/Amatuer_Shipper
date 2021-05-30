package com.example.AmateurShipper.Adapter;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AmateurShipper.Activity.MainActivity;
import com.example.AmateurShipper.Model.NotificationWebObject;
import com.example.AmateurShipper.Model.PostObject;
import com.example.AmateurShipper.R;
import com.example.AmateurShipper.Util.FormatName;
import com.example.AmateurShipper.Util.NotificationPublisher;
import com.example.AmateurShipper.Util.PostDiffUtilCallback;
import com.example.AmateurShipper.Util.formatTimeStampToDate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mapbox.services.android.navigation.ui.v5.feedback.FeedbackBottomSheet.TAG;

//import static com.example.AmateurShipper.LoginActivity.IDUSER;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewAdapterClass> {
    //private RecyclerViewClickInterface recyclerViewClickInterface;
    List<PostObject> postList;
    Context mContext;
    public int get_position;
    int level,countPost;
    String IdUser;
    public MainActivity mainActivity;
    private OnPostListener mOnPostListener;
    SharedPreferences sharedpreferencesCurrentCountReceived;
    FirebaseDatabase rootDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = rootDatabase.getReference();
    private FirebaseFirestore mFireStore;

    public static final String countPostReceived = "0";
    public static int star1 = 0;
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
        String name = postObject.getTen_nguoi_gui();
        FormatName fmn = new FormatName();
        viewAdapterClass.name_poster.setText(fmn.formatName(name));
        formatTimeStampToDate fm = new formatTimeStampToDate();
        viewAdapterClass.time.setText(fm.convertToDay(Long.parseLong(postObject.getThoi_gian())));
        viewAdapterClass.start_post.setText(formatAddress(postObject.getNoi_nhan()));
        viewAdapterClass.end_post.setText(formatAddress(postObject.getNoi_giao()));
        viewAdapterClass.distance.setText(String.valueOf(postObject.getKm()));
        viewAdapterClass.fee.setText(String.valueOf(postObject.getPhi_giao()));
        viewAdapterClass.payment.setText(String.valueOf(postObject.getPhi_ung()));
        viewAdapterClass.note.setText(postObject.getGhi_chu());
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
        holder.itemView.startAnimation(animation);
    }
    @Override
    public int getItemCount() {
        return postList.size();
    }
    public class ViewAdapterClass extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_poster, time, start_post, end_post, distance, fee, payment, note,tvcount;
        CircleImageView image_poster;
        Button get_order, attach_image;
        OnPostListener onPostListener;
        public ViewAdapterClass(@NonNull final View itemView, OnPostListener onPostListener) {
            super(itemView);
           // sharedpreferencesIdUser = itemView.getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            mainActivity = (MainActivity) itemView.getContext();
            mFireStore = FirebaseFirestore.getInstance();
            sharedpreferencesCurrentCountReceived = itemView.getContext().getSharedPreferences(countPostReceived, Context.MODE_PRIVATE);
            name_poster = itemView.findViewById(R.id.name_poster);
            time = itemView.findViewById(R.id.time_post);
            start_post = itemView.findViewById(R.id.txt_start_place);
            end_post = itemView.findViewById(R.id.txt_end_place);
            distance = itemView.findViewById(R.id.txt_distance);
            //  quantity = itemView.findViewById(R.id.txt_quantity);
            fee = itemView.findViewById(R.id.txt_fee);
            payment = itemView.findViewById(R.id.txt_payment);
            note = itemView.findViewById(R.id.txt_note);
            tvcount = itemView.findViewById(R.id.tv_count);
            image_poster = itemView.findViewById(R.id.img_poster);
            get_order = itemView.findViewById(R.id.img_getOrder);
            // attach_image = itemView.findViewById(R.id.img_attachment_image);
            getUid();
            loadData();
            clearData();

            this.onPostListener = onPostListener;
            itemView.setOnClickListener(this);
            get_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentReference docRef = mFireStore.collection("ProfileShipper").document(IdUser);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                Long tsLong = System.currentTimeMillis() / 1000;
                                String timestamp = tsLong.toString();
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    document.getData();
                                    String role = document.get("role").toString();
                                    if (role.equals("1")) {
                                        loadData();
                                        level =Integer.parseInt(document.get("level").toString())+2;
                                        if (countPost < level) {
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
                                            String receiveLat = postList.get(getAdapterPosition()).receiveLat;
                                            String receiveLng = postList.get(getAdapterPosition()).receiveLng;
                                            String shipLat = postList.get(getAdapterPosition()).shipLat;
                                            String shipLng = postList.get(getAdapterPosition()).shipLng;
                                            String estimateTime = postList.get(getAdapterPosition()).getTime_estimate();
                                            Log.i(TAG, "AAAAAAA: "+estimateTime);

                                            //String estimateTime = postList.get(getAdapterPosition()).time_estimate;
                                            PostObject postObject = new PostObject(ten_nguoi_gui, sdt_nguoi_gui, noi_nhan, noi_giao, sdt_nguoi_nhan,
                                                    ten_nguoi_nhan, ghi_chu, timestamp, id_shop, phi_giao, phi_ung, km, id_post, "0",receiveLat,
                                                    receiveLng,shipLat,shipLng,estimateTime);
                                            databaseReference.child("received_order_status").child(IdUser).child(postObject.getId_post()).setValue(postObject);
                                            databaseReference.child("newsfeed").child(postObject.getId_post()).setValue(null);
                                            postList.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            mainActivity.setCountOrder(mainActivity.getmCountOrder() + 1);

                                            NotificationWebObject noti = new NotificationWebObject(id_post, IdUser, "1", timestamp);
                                            databaseReference.child("Transaction").child(postObject.getId_post()).child("id_shipper").setValue(IdUser);
                                            databaseReference.child("OrderStatus").child(postObject.getId_shop()).child(postObject.getId_post()).child("status").setValue("1");
                                            databaseReference.child("OrderStatus").child(postObject.getId_shop()).child(postObject.getId_post()).child("read").setValue(0);
                                            databaseReference.child("Notification").child(id_shop).push().setValue(noti);
                                            databaseReference.child("Transaction").child(postObject.getId_post()).child("status").setValue("1");
                                            int id = (int) new Date().getTime();
                                            int estimated;
                                            if (!TextUtils.isEmpty(estimateTime) && TextUtils.isDigitsOnly(estimateTime)) {
                                                estimated = Integer.parseInt(estimateTime)*1000;
                                            } else {
                                                estimated = 0;
                                            }
                                            scheduleNotification(notification(ten_nguoi_gui,estimateTime),20000 ,estimateTime,id, id);
                                            Log.i(TAG, "onComplete: estimated " + estimated);
                                            Log.i(TAG, "onComplete: schedule  created" + estimateTime);
                                            //countPost++;
                                            //saveData(String.valueOf(countPost));
                                        } else {
                                            Toast.makeText(mContext.getApplicationContext(), "Bạn không thể nhận thêm", Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(mContext, "Tài khoản tạm thời bị khóa chức năng này", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }


    public void saveData(String count){
        SharedPreferences.Editor editor = sharedpreferencesCurrentCountReceived.edit();
        editor.putString(countPostReceived, count);
        editor.commit();
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferencesCurrentCountReceived.edit();
        editor.clear();
        editor.commit();
    }

    private void loadData() {
            countPost = Integer.parseInt(sharedpreferencesCurrentCountReceived.getString(countPostReceived, "0"));
    }
    public interface OnPostListener {
        void onPostClick(int position);
    }

    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        IdUser = user.getUid();
    }

    public String formatAddress(String address){
        String[] strArr = address.split("[,]");
        String street,ward,distreet,city;
        street = strArr[0];
        ward = strArr[1];
        distreet = strArr[2];
        ward = ward.substring(8,ward.length());
        distreet = distreet.substring(5,distreet.length());
        return street + ", " + ward + ", "+distreet;
    }
    private void scheduleNotification(Notification notification, long delay, String id_channel, int request_code, int noti_id) {
        Intent notificationIntent = new Intent(mContext.getApplicationContext(), NotificationPublisher.class);
        notificationIntent.putExtra("NOTIFICATION_ID", noti_id);
        notificationIntent.putExtra("NOTIFICATION", notification);
        notificationIntent.putExtra("CHANNEL_ID", id_channel);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), request_code, notificationIntent, 0);
//      long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)mContext.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        long timeAtButtonclick = System.currentTimeMillis() + delay;
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonclick, pendingIntent);
//      mContext.getApplicationContext().sendBroadcast(notificationIntent);
    }
    public Notification notification(String content,String id_channel){
        String title = "Đơn hàng của ";

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(id_channel,"n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = mContext.getApplicationContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext,id_channel)
                .setSmallIcon(R.drawable.noti)
                .setContentTitle(title)
                .setContentText(content + " đang bị chậm trễ.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
        return builder.build();
    }
}
