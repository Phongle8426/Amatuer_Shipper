package com.example.AmateurShipper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AmateurShipper.Dialog.FilterPaymentDialog;
import com.example.AmateurShipper.Util.PostDiffUtilCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
//import static com.example.AmateurShipper.LoginActivity.IDUSER;
import static com.example.AmateurShipper.LoginActivity.MyPREFERENCES;
import static com.example.AmateurShipper.LoginActivity.USERNAME;

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
    String iDUser;
    int index = -1;
    LinearLayoutManager mLayoutManager;
    RecyclerView NewsRecyclerview;
    PostAdapter postAdapter;
   private List<PostObject> mData = new ArrayList<>();
   final ArrayList<String> mLocationItem = new ArrayList<>();
   final String[] location = {"Hai Chau","Thanh Khe","Cam Le","Hoa Khanh"};
   boolean selected[] = new boolean[]{false, false, false, false};
    com.getbase.floatingactionbutton.FloatingActionButton btn_filter_location,btn_filter_payment;
    ImageView btn_notify_new_order;
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
        btn_filter_location = view.findViewById(R.id.btn_filter_location);
        btn_filter_payment = view.findViewById(R.id.btn_filter_payment);
        btn_notify_new_order = view.findViewById(R.id.btn_notify_new_order);
        NewsRecyclerview = view.findViewById(R.id.rcv_post);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFireStore = FirebaseFirestore.getInstance();
        getUid();
        //loadStar();
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mLayoutManager.setReverseLayout(true);
        NewsRecyclerview.setHasFixedSize(true);
        mLayoutManager.setStackFromEnd(true);
        NewsRecyclerview.setLayoutManager(mLayoutManager);
        getChildList();
        loadshimer();
        postAdapter = new PostAdapter(mData, getContext(), this);
        NewsRecyclerview.setAdapter(postAdapter);

        checkScroll();
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
                filterPaymentDialog.setTargetFragment(HomeFragment.this,1);
                filterPaymentDialog.show(getFragmentManager(),"filter by payment");
            }
        });

        btn_notify_new_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsRecyclerview.scrollToPosition(NewsRecyclerview.getAdapter().getItemCount()-1);
                btn_notify_new_order.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

//    public void loadStar(){
//        mDatabase.child("Ratting_Star").child(iDUser).addValueEventListener(new ValueEventListener() {
//            int star1=0;
//            String star;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    for (DataSnapshot snap : snapshot.getChildren()){
//                        star1 += Integer.parseInt(snap.getValue(String.class));
//                    }
//                    if (star1 > 0 && star1 <= 50)
//                        star="2";
//                    if (star1 >= 50 && star1 < 100)
//                        star="3";
//                    if (star1 >= 100 && star1 < 150)
//                        star="4";
//                    if (star1 >= 150)
//                        star="5";
//                    Log.i(TAG, "onDataChangeSSSSS: "+ star);
//                    mFireStore.collection("ProfileShipper").document(iDUser).update("rate_star",star);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    //Load ID User
    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        iDUser = user.getUid();
    }

    public void getChildList(){
        mDatabase.child("newsfeed").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    PostObject data = snapshot.getValue(PostObject.class);
                    if(insertList1.isEmpty()){
                        if(filter_payment !=0){ // kiểm tra giá trị lọc theo tiền ứng có lớn hơn 0 hay ko, nếu ko thì kiểm tra điều kiện lọc vị trí
                            if(filter_payment >= Integer.parseInt(data.getPhi_ung())){
                                insertList1.add(data);
                            }
                        }else if (mLocationItem.size()>0){ // kiểm tra list lọc vị trí có trống hay không, nếu k thì bỏ qua lọc
                            for (int k = 0;k<mLocationItem.size();k++){
                                if(data.getNoi_nhan().contains(mLocationItem.get(k)))
                                    insertList1.add(data);
                            }
                        } else insertList1.add(data);
                        postAdapter.addItem(0,data);
                    }
                    else{
                        if(filter_payment !=0){ // kiểm tra giá trị lọc theo tiền ứng có lớn hơn 0 hay ko, nếu ko thì kiểm tra điều kiện lọc vị trí
                            if(filter_payment >= Integer.parseInt(data.getPhi_ung())){
                                insertList1.add(data);
                            }
                        }else if (mLocationItem.size()>0){ // kiểm tra list lọc vị trí có trống hay không, nếu k thì bỏ qua lọc
                            for (int k = 0;k<mLocationItem.size();k++){
                                if(data.getNoi_nhan().contains(mLocationItem.get(k)))
                                    insertList1.add(data);
                            }
                        } else insertList1.add(data);
                        postAdapter.addItem(postAdapter.getItemCount(),data);
                        if (index == -1){
                            NewsRecyclerview.scrollToPosition(NewsRecyclerview.getAdapter().getItemCount()-1);
                            btn_notify_new_order.setVisibility(View.INVISIBLE);
                        }else if (index < NewsRecyclerview.getAdapter().getItemCount()-2){
                            btn_notify_new_order.setVisibility(View.VISIBLE);
                            Log.i(TAG, "position: "+ index + "\n"+NewsRecyclerview.getAdapter().getItemCount());
                        }else NewsRecyclerview.scrollToPosition(NewsRecyclerview.getAdapter().getItemCount()-1);
                    }
                }
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
        },1000);
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
                            Toast.makeText(getContext(), "can't add", Toast.LENGTH_SHORT).show();
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
}