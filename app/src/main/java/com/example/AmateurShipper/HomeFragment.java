package com.example.AmateurShipper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView NewsRecyclerview;
     PostAdapter postAdapter;
   public List<PostObject> mData;

    com.getbase.floatingactionbutton.FloatingActionButton btn_filter_location,btn_filter_payment;
    private DatabaseReference mDatabase;


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       // mData = new ArrayList<>();


        //postAdapter = new PostAdapter(mData);
       // NewsRecyclerview.setAdapter(postAdapter);
       // NewsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        btn_filter_location = view.findViewById(R.id.btn_filter_location);
        btn_filter_payment = view.findViewById(R.id.btn_filter_payment);
        NewsRecyclerview = view.findViewById(R.id.rcv_post);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mData = new ArrayList<>();
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));

     //   PostAdapter postAdapter = new PostAdapter(mData,getContext());
//        NewsRecyclerview.setAdapter(postAdapter);
       // newPostAdapter();
        NewsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(mData, getContext());
        NewsRecyclerview.setAdapter(postAdapter);

        getListNewsFeed();
        //getList();
        btn_filter_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        btn_filter_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPaymen();
            }
        });


        return view;
    }



//    public void newPostAdapter(){
//        postAdapter = new PostAdapter(mData, getContext());
//    }
    public void getList(){
        mDatabase.child("newsfeed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
//                    PostObject data = snapshot.getValue(PostObject.class);
//                    mData.add(data);
//                    postAdapter.insertData(mData);
//                }else{
//                    Toast.makeText(getContext(), "khong the load", Toast.LENGTH_LONG).show();
//                }
                if (snapshot.exists()) {
                    Toast.makeText(getContext(), "ok", Toast.LENGTH_LONG).show();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PostObject data = dataSnapshot.getValue(PostObject.class);
//                        String ghichu = dataSnapshot.child("ghi_chu").getValue(String.class);
//                        String idshop = dataSnapshot.child("id_shop").getValue(String.class);
//                        String nguoigui = dataSnapshot.child("ten_nguoi_gui").getValue(String.class);
//                        String sdtnguoigui = dataSnapshot.child("sdt_nguoi_gui").getValue(String.class);
//                        String nguoinhan = dataSnapshot.child("ten_nguoi_nhan").getValue(String.class);
//                        String sdtnguoinhan = dataSnapshot.child("sdt_nguoi_nhan").getValue(String.class);
//                        String noigiao = dataSnapshot.child("noi_giao").getValue(String.class);
//                        String noinhan = dataSnapshot.child("noi_nhan").getValue(String.class);
//                        String phigiao = dataSnapshot.child("phi_giao").getValue(String.class);
//                        String phiung = dataSnapshot.child("phi_ung").getValue(String.class);
//                        String thoigian = dataSnapshot.child("thoi_gian").getValue(String.class);
//                        String km = dataSnapshot.child("km").getValue(String.class);
//
//                        PostObject data = new PostObject(nguoigui,sdtnguoigui,noinhan,noigiao,sdtnguoinhan,
//                                nguoinhan,ghichu,thoigian,idshop,phigiao,phiung,km);
                        mData.add(data);
                    }
//                    newPostAdapter();
//                    NewsRecyclerview.setAdapter(postAdapter);
                    postAdapter.insertData(mData);
                }else{
                    Toast.makeText(getContext(), "khong the load", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getListNewsFeed(){
        mDatabase.child("newsfeed").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()){
                        PostObject data = snapshot.getValue(PostObject.class);
                        mData.add(data);
                        postAdapter.insertData(mData);
                    }else{
                    Toast.makeText(getContext(), "khong the load", Toast.LENGTH_LONG).show();
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

    public void showDialogPaymen(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final SeekBar seek = new SeekBar(getContext());
        builder.setTitle("Lọc theo tiền Ứng");
        seek.setMax(255);
        seek.setKeyProgressIncrement(10);
        builder.setView(seek);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Toast.makeText(getContext(), "Ứng <= "+ i+"K", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Lọc theo vị trí");

        final String[] location = {"Hai Chau","Thanh Khe","Cam Le","Hoa Khanh"};
        builder.setSingleChoiceItems(location, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        Toast.makeText(getContext(),location[0], Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getContext(),location[1], Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getContext(),location[2], Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getContext(),location[3], Toast.LENGTH_SHORT).show();
                        break;
                    default: break;
                }
            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

}