package com.example.AmateurShipper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
//import static com.example.AmateurShipper.LoginActivity.IDUSER;
//import static com.example.AmateurShipper.LoginActivity.MyPREFERENCES;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab_dang_giao#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab_dang_giao extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView TabDGiaoRecyclerview;
    ShippingOrderAdapter shippingOrderAdapter;
    DatabaseReference mDatabase;
    FragmentManager fm;
    List<PostObject> mListData = new ArrayList<>() ;
    String iDUser;
    public tab_dang_giao() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab_chua_nhan.
     */
    // TODO: Rename and change types and number of parameters
    public static tab_dang_giao newInstance(String param1, String param2) {
        tab_dang_giao fragment = new tab_dang_giao();
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
        //sharedpreferencesIdUser = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_tab_dang_giao,container,false);
        TabDGiaoRecyclerview = view.findViewById(R.id.rcv_tab_dang_giao);
        mDatabase = FirebaseDatabase.getInstance().getReference();
       // mainActivity = (MainActivity) getActivity();
       // mainActivity.setCountOrder(0);
        //mainActivity.disableNotification();
        getUid();

        fm = getActivity().getSupportFragmentManager();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        TabDGiaoRecyclerview.setHasFixedSize(true);
        TabDGiaoRecyclerview.setLayoutManager(mLayoutManager);
        if(mListData!=null){
            mListData.clear();
        }
        getListOrder();
        return view;
    }

    //  Load ID User
    public void getUid(){
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    iDUser = user.getUid();
    }

    public void getListOrder(){
        mDatabase.child("received_order_status").child(iDUser).orderByChild("status").equalTo("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PostObject data = dataSnapshot.getValue(PostObject.class);
                        mListData.add(data);
                    }
                    shippingOrderAdapter = new ShippingOrderAdapter(mListData, tab_dang_giao.this,fm);
                    TabDGiaoRecyclerview.setAdapter(shippingOrderAdapter);
                    shippingOrderAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(getContext(), "Khong the tai", Toast.LENGTH_SHORT).show();
                }

                mDatabase.child("received_order_status").child(iDUser).removeEventListener(this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}