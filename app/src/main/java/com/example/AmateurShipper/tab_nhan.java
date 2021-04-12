package com.example.AmateurShipper;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab_nhan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab_nhan extends Fragment implements statusInterfaceRecyclerView, ReceivedOrderAdapter.OnReceivedOderListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView NewsRecyclerview;
    MainActivity mainActivity;
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    List<PostObject> mData = new ArrayList<>();
    ReceivedOrderAdapter receivedOrderAdapter;
    ChatFragment chatFragment;
    public tab_nhan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab_nhan.
     */
    // TODO: Rename and change types and number of parameters
    public static tab_nhan newInstance(String param1, String param2) {
        tab_nhan fragment = new tab_nhan();
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_nhan,container,false);
        NewsRecyclerview = view.findViewById(R.id.rcv_tab_nhan);
        mainActivity = (MainActivity) getActivity();
        mainActivity.setCountOrder(0);
        mainActivity.disableNotification();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        NewsRecyclerview.setHasFixedSize(true);
        mLayoutManager.setStackFromEnd(true);
        NewsRecyclerview.setLayoutManager(mLayoutManager);
        if(mData!=null){
            mData.clear();
        }
        getListStatusReceived();
        receivedOrderAdapter = new ReceivedOrderAdapter(mData, tab_nhan.this,this, fm);

        NewsRecyclerview.setAdapter(receivedOrderAdapter);
        NewsRecyclerview.smoothScrollToPosition(0);

        //mData = new ArrayList<>();
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));
//        mData.add(new PostObject("Van Phong","10 phut","285/33 Tran Cao Van - Dan Nang","36/8 Pham Van Nghi Da Nang","3KM","Dua tay day naf, mai ben nhau ban nho",R.drawable.anhhhhh,R.drawable.anhhhhh,10,15000,15000));

        return view;
    }

    public void getListStatusReceived(){
        mDatabase = rootNode.getReference();
        mDatabase.child("received_order_status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mData = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PostObject data = dataSnapshot.getValue(PostObject.class);
                        mData.add(data);
                    }
                    receivedOrderAdapter.insertData(mData);

                }else{
                   // Toast.makeText(getContext(), "khong the load", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {

        Toast.makeText(getContext(), "Tina muon an shit", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongItemClick(int position) {

    }



    @Override
    public void onReceivedItem(int position){
        Toast.makeText(getContext(), "hello con cac", Toast.LENGTH_SHORT).show();
    }
}