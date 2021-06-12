package com.example.AmateurShipper.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.AmateurShipper.Adapter.HistoryOrderAdapter;
import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;
import com.example.AmateurShipper.Model.PostObject;
import com.example.AmateurShipper.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.AmateurShipper.Fragment.tab_nhan.idpostvalue;
import static com.example.AmateurShipper.Fragment.tab_nhan.idtabvalue;
import static com.example.AmateurShipper.Fragment.tab_nhan.idtshopvalue;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab_lich_su#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab_lich_su extends Fragment implements statusInterfaceRecyclerView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView TabLSuRecyclerview;
    RelativeLayout lichSuLayout;
    ShimmerFrameLayout layout_shimmer;
    ImageView empty;
    DatabaseReference mDatabase;
    HistoryOrderAdapter historyOrderAdapter;
    FragmentManager fm;
    FrameLayout framChat;
    List<PostObject> mListData = new ArrayList<>() ;
    String iDUser;

    public tab_lich_su() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab_lich_su.
     */
    // TODO: Rename and change types and number of parameters
    public static tab_lich_su newInstance(String param1, String param2) {
        tab_lich_su fragment = new tab_lich_su();
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
        View view = inflater.inflate(R.layout.fragment_tab_lich_su,container,false);
        TabLSuRecyclerview = view.findViewById(R.id.rcv_tab_lich_su);
        lichSuLayout = view.findViewById(R.id.lich_su_layout);
        framChat =view.findViewById(R.id.frag_container_detail);
        layout_shimmer = view.findViewById(R.id.shimmer_status);
        empty = view.findViewById(R.id.data_not_found);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getUid();

        fm = getActivity().getSupportFragmentManager();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        //mLayoutManager.setReverseLayout(true);
        //mLayoutManager.setStackFromEnd(true);
        //TabLSuRecyclerview.setHasFixedSize(true);
        TabLSuRecyclerview.setLayoutManager(mLayoutManager);
        if(mListData!=null){
            mListData.clear();
        }
        getListOrderHistory();
        //loadshimer();
        return view;
    }

    public void createNewAdapter(List<PostObject> arrayList){
        historyOrderAdapter = new HistoryOrderAdapter(arrayList, tab_lich_su.this,fm,this);
    }
    ArrayList<PostObject> arrayList = new ArrayList<>();
    public void getListOrderHistory() {
        mListData.clear();
        mDatabase.child("received_order_status").child(iDUser).orderByChild("status").startAt("2").endAt("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mListData.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PostObject data = dataSnapshot.getValue(PostObject.class);
                        mListData.add(data);
                    }
                    Collections.sort(mListData);
                    createNewAdapter(mListData);
                    historyOrderAdapter.notifyDataSetChanged();
                    TabLSuRecyclerview.setAdapter(historyOrderAdapter);
                } else {
                    empty.setVisibility(View.VISIBLE);
                }
                layout_shimmer.stopShimmer();
                layout_shimmer.hideShimmer();
                layout_shimmer.setVisibility(View.GONE);
                TabLSuRecyclerview.setVisibility(View.VISIBLE);
                mDatabase.child("received_order_status").child(iDUser).removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        iDUser = user.getUid();
    }

    @Override
    public void onItemClick(int position) {
        String idPost = mListData.get(position).getId_post();
        String idShop = mListData.get(position).getId_shop();
        Log.i(TAG, "onItemClick: "+idPost);
        lichSuLayout.setVisibility(View.GONE);
         //framChat.setVisibility(View.VISIBLE);
        DetailOrderFragment detailFragment = new DetailOrderFragment();
        Bundle bundle = new Bundle();

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        bundle.putString(idpostvalue,idPost); // use as per your need
        bundle.putString(idtshopvalue,idShop);
        bundle.putString(idtabvalue,"tablichsu");
        detailFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frame_cart,detailFragment);
        fragmentTransaction.commit();
    }

    public void loadshimer(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_shimmer.stopShimmer();
                layout_shimmer.hideShimmer();
                layout_shimmer.setVisibility(View.GONE);
                TabLSuRecyclerview.setVisibility(View.VISIBLE);
            }
        },1500);
    }

    @Override
    public void onLongItemClick(int position) {

    }
}