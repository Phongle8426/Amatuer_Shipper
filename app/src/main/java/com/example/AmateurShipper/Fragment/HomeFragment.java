package com.example.AmateurShipper.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AmateurShipper.Dialog.FilterPaymentDialog;
import com.example.AmateurShipper.Adapter.PostAdapter;
import com.example.AmateurShipper.Model.PostObject;
import com.example.AmateurShipper.R;
import com.example.AmateurShipper.Util.LocationService;
import com.example.AmateurShipper.Util.NetworkChangeListener;
import com.example.AmateurShipper.Util.formatTimeStampToDate;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
//import static com.example.AmateurShipper.Activity.LoginActivity.IDUSER;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements PostAdapter.OnPostListener,FilterPaymentDialog.OnInputSelected {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ShimmerFrameLayout layout_shimmer;
    LinearLayout layout_block;
    String iDUser;
    int index = -1;
    LinearLayoutManager mLayoutManager;
    RecyclerView NewsRecyclerview;
    PostAdapter postAdapter;
   private List<PostObject> mData = new ArrayList<>();
   final ArrayList<String> mLocationItem = new ArrayList<>();
   final String[] location = {"Thanh Khê","Hải Châu","Sơn Trà","Liên Chiểu","Cẩm Lệ","Ngũ Hành Sơn","Hoà Vang"};
   boolean selected[] = new boolean[]{false, false, false, false};
    com.getbase.floatingactionbutton.FloatingActionButton btn_filter_location,btn_filter_payment;
    ImageView btn_notify_new_order;
    TextView time_block,tv_reason;
    private DatabaseReference mDatabase;
    private FirebaseFirestore mFireStore;
    public int filter_payment = 0, rate_score;
    List<PostObject> insertList1 = new ArrayList<>();

    public HomeFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
}

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       // mData = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        layout_shimmer = view.findViewById(R.id.shimmer_newfeed);
        layout_block = view.findViewById(R.id.layout_block);
        btn_filter_location = view.findViewById(R.id.btn_filter_location);
        btn_filter_payment = view.findViewById(R.id.btn_filter_payment);
        btn_notify_new_order = view.findViewById(R.id.btn_notify_new_order);
        time_block = view.findViewById(R.id.time_block);
        tv_reason = view.findViewById(R.id.tv_reason);
        NewsRecyclerview = view.findViewById(R.id.rcv_post);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFireStore = FirebaseFirestore.getInstance();
        //availableInternet isAvailable = new availableInternet();
            getUid();
            checkBlock();
            loadStar();
            //deleteOrder();
            mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mLayoutManager.setReverseLayout(true);
            NewsRecyclerview.setHasFixedSize(true);
            mLayoutManager.setStackFromEnd(true);
            NewsRecyclerview.setLayoutManager(mLayoutManager);
            getChildList();
            //loadshimer();
            postAdapter = new PostAdapter(mData, getContext(), this);
            NewsRecyclerview.setAdapter(postAdapter);
            checkScroll();
            getLocation();

            btn_filter_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLocationItem.clear();
                    showDialog();
                }
            });
            btn_filter_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FilterPaymentDialog filterPaymentDialog = new FilterPaymentDialog();
                    filterPaymentDialog.setTargetFragment(HomeFragment.this, 1);
                    filterPaymentDialog.show(getFragmentManager(), "filter by payment");
                }
            });

            btn_notify_new_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsRecyclerview.scrollToPosition(NewsRecyclerview.getAdapter().getItemCount() - 1);
                    btn_notify_new_order.setVisibility(View.INVISIBLE);
                }
            });

            return view;
    }

    public void getLocation(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        Intent intent = new Intent(getActivity(), LocationService.class);
        intent.setAction("Start");
        getContext().startService(intent);
    }
    public void deleteOrder(){
        mDatabase.child("received_order_status").child(iDUser).orderByChild("status").equalTo("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    String de = ds.child("id_post").getValue(String.class);
                    mDatabase.child("received_order_status").child(iDUser).child(de).setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void checkBlock(){
        DocumentReference docRef = mFireStore.collection("ProfileShipper").document(iDUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.getData();
                        String role = document.get("role").toString();
                        if (role.equals("2")){
                            formatTimeStampToDate fm = new formatTimeStampToDate();
                            String time = fm.convertTimeForBlock(Long.parseLong(document.get("lock_time").toString()));
                            String reason = document.get("reason").toString();
                            Long current_time = System.currentTimeMillis() / 1000;
                            long lock_time= Long.parseLong(document.get("lock_time").toString());
                            if (current_time < lock_time) {
                                layout_block.setVisibility(View.VISIBLE);
                                tv_reason.setText(reason);
                                time_block.setText(time);
                            }else {
                                docRef.update("role","1");
                                docRef.update("lock_time","");
                                docRef.update("reason","");
                            }
                        }
                    }
                }
            }
        });
    }
    public void loadStar(){
        mDatabase.child("Ratting_Star").child(iDUser).addValueEventListener(new ValueEventListener() {
            double sumStar=0;
            double countRate=0;
            double star;
            int sumPoint =0;
            int mlevel;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snap : snapshot.getChildren()){
                        int mtp = snap.child("rate").getValue(Integer.class);
                        if (mtp==5)
                            sumPoint+=3;
                        else if (mtp==4)
                            sumPoint+=2;
                        else if (mtp==3)
                            sumPoint+=1;
                        else if (mtp==2)
                            sumPoint-=2;
                        else if (mtp==1)
                            sumPoint-=3;

                        sumStar+=snap.child("rate").getValue(Double.class);
                        countRate++;
                    }
                    if (sumPoint < 50){
                        mlevel =2;
                    }
                    if (sumPoint >= 50 && sumPoint < 100){
                        mlevel =3;
                    }
                    if (sumPoint >= 100 && sumPoint < 150){
                        mlevel =4;
                    }
                    if (sumPoint >= 150){
                        mlevel =5;
                    }
                    star=sumStar/countRate;
                    star = Math.ceil((star * 10.0))/10.0;
                    mFireStore.collection("ProfileShipper").document(iDUser).update("rate_star",star);
                    mFireStore.collection("ProfileShipper").document(iDUser).update("level",mlevel);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Load ID User
    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        iDUser = user.getUid();
    }

    public void getChildList(){
        Long currentTimestamp = System.currentTimeMillis()/1000;
        mDatabase.child("newsfeed").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    PostObject data = snapshot.getValue(PostObject.class);
                    if (Long.parseLong(data.getThoi_gian())-currentTimestamp < 86400) {
                        if (insertList1.isEmpty()) {
                            if (filter_payment != 0) { // kiểm tra giá trị lọc theo tiền ứng có lớn hơn 0 hay ko, nếu ko thì kiểm tra điều kiện lọc vị trí
                                if (filter_payment >= Integer.parseInt(data.getPhi_ung())) {
                                    insertList1.add(data);
                                }
                            } else if (mLocationItem.size() > 0) { // kiểm tra list lọc vị trí có trống hay không, nếu k thì bỏ qua lọc
                                for (int k = 0; k < mLocationItem.size(); k++) {
                                    if (data.getNoi_nhan().contains(mLocationItem.get(k)))
                                        insertList1.add(data);
                                }
                            } else insertList1.add(data);
                            postAdapter.addItem(0, data);
                        } else {
                            if (filter_payment != 0) { // kiểm tra giá trị lọc theo tiền ứng có lớn hơn 0 hay ko, nếu ko thì kiểm tra điều kiện lọc vị trí
                                if (filter_payment >= Integer.parseInt(data.getPhi_ung())) {
                                    insertList1.add(data);
                                }
                            } else if (mLocationItem.size() > 0) { // kiểm tra list lọc vị trí có trống hay không, nếu k thì bỏ qua lọc
                                for (int k = 0; k < mLocationItem.size(); k++) {
                                    if (data.getNoi_nhan().contains(mLocationItem.get(k)))
                                        insertList1.add(data);
                                }
                            } else insertList1.add(data);
                            postAdapter.addItem(postAdapter.getItemCount(), data);
                            if (index == -1) {
                                NewsRecyclerview.scrollToPosition(NewsRecyclerview.getAdapter().getItemCount() - 1);
                                btn_notify_new_order.setVisibility(View.INVISIBLE);
                            } else if (index < NewsRecyclerview.getAdapter().getItemCount() - 2) {
                                btn_notify_new_order.setVisibility(View.VISIBLE);
                                Log.i(TAG, "position: " + index + "\n" + NewsRecyclerview.getAdapter().getItemCount());
                            } else
                                NewsRecyclerview.scrollToPosition(NewsRecyclerview.getAdapter().getItemCount() - 1);
                        }
                    }
                }
                layout_shimmer.stopShimmer();
                layout_shimmer.hideShimmer();
                layout_shimmer.setVisibility(View.GONE);
                NewsRecyclerview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        loadshimer();
    }

    public void loadshimer(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_shimmer.stopShimmer();
                layout_shimmer.hideShimmer();
                layout_shimmer.setVisibility(View.GONE);
                NewsRecyclerview.setVisibility(View.VISIBLE);
            }
        },4000);
    }

    public void checkScroll(){
        NewsRecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, "onScrolled: "+mLayoutManager.findLastVisibleItemPosition());
                index = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    public void showDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Lọc theo vị trí");
        final boolean[] checkedItems = new boolean[location.length];

        builder.setMultiChoiceItems(location, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            int count= 0;
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean ischecked) {
                if (ischecked){
                        if (mLocationItem.size()<3){
                            mLocationItem.add(String.valueOf(location[position]));
                            checkedItems[position]=true;
                            count++;
                        }else{
                            ((AlertDialog) dialogInterface).getListView().setItemChecked(position, false);
                            checkedItems[position]=false;
                            Toast.makeText(getContext(), "tối đa", Toast.LENGTH_SHORT).show();
                        }
                }else {
                    count--;
                    mLocationItem.remove(String.valueOf(location[position]));
                    checkedItems[position]=false;
                }
            }
        }).setPositiveButton("Lọc", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                filter_payment = 0; // xóa đi điều kiện lọc theo tiền
                getChildList();
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    @Override
    public void onPostClick(int position) {
        mData.get(position);
        getChildList(); // lọc lại
    }

    @Override
    public void sendInput(String dialog_payment) {
        mLocationItem.clear(); // xóa đi điều kiện lọc theo vị trí
        getChildList(); // lọc lại
        filter_payment = Integer.parseInt(dialog_payment);
    }

    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    public void onStop() {
        requireActivity().unregisterReceiver(networkChangeListener);

        super.onStop();
    }
}