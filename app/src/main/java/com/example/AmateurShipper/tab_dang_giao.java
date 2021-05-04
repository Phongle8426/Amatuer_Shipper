package com.example.AmateurShipper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import Helper.MyButtonClickListner;
import Helper.MySwipeHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.AmateurShipper.Dialog.SecurityCodeDialog;
import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;
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
import static com.example.AmateurShipper.tab_nhan.idpostvalue;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab_dang_giao#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab_dang_giao extends Fragment implements statusInterfaceRecyclerView,SecurityCodeDialog.OnInputSelected {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String securitycodeToSecurity = "1234";
    public static final String idpostToSecurity = "111";
    public static final String idshopToSecurity = "222";
    public static final String positionToSecurity = "333";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView TabDGiaoRecyclerview;
    ShippingOrderAdapter shippingOrderAdapter;
    DatabaseReference mDatabase;
    FragmentManager fm;
    FrameLayout framChat;
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
        View view = inflater.inflate(R.layout.fragment_tab_dang_giao,container,false);
        TabDGiaoRecyclerview = view.findViewById(R.id.rcv_tab_dang_giao);
        framChat =view.findViewById(R.id.frag_container_detail);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getUid();
        fm = getActivity().getSupportFragmentManager();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        TabDGiaoRecyclerview.setHasFixedSize(true);
        TabDGiaoRecyclerview.setLayoutManager(mLayoutManager);
        MySwipeHelper mySwipeHelper = new MySwipeHelper(getActivity(), TabDGiaoRecyclerview , 200) {
            @Override
            public void instaniatMyButton(final RecyclerView.ViewHolder viewHolder, List<MyButton> buff) {
                buff.add(new MyButton("Hoan Thanh", 30, Color.parseColor("#DC143C"), new MyButtonClickListner(){
                    @Override
                    public void onClick(final int pos) {
                        openSecurityCode(pos);
                    }
                }, getContext()));
            }
        };

        if(mListData!=null){
            mListData.clear();
        }
        getListOrder();
        return view;
    }

    public void openSecurityCode(int pos){
        String idpost;
        String idshop;
        idpost = mListData.get(pos).getId_post();
        idshop = mListData.get(pos).getId_shop();
        Log.i(TAG, "openSecurityCode: " + idpost +"/" +idshop);
        mDatabase.child("Transaction").child(idpost).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String securitycode = snapshot.child("ma_bi_mat").getValue(String.class);
                mDatabase.child("Transaction").child(idpost).removeEventListener(this);
                Bundle args = new Bundle();
                args.putString(securitycodeToSecurity, securitycode);
                args.putString(idpostToSecurity,idpost);
                args.putString(idshopToSecurity,idshop);
                args.putInt(positionToSecurity,pos);
                SecurityCodeDialog securityCodeDialog = new SecurityCodeDialog();
                securityCodeDialog.setTargetFragment(tab_dang_giao.this,1);
                securityCodeDialog.setArguments(args);
                securityCodeDialog.show(getFragmentManager(),"security code");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    //  Load ID User
    public void getUid(){
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    iDUser = user.getUid();
    }

    public void createNewAdapter(){
        shippingOrderAdapter = new ShippingOrderAdapter(mListData, tab_dang_giao.this,fm,this);
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
                    createNewAdapter();
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

    @Override
    public void onItemClick(int position) {
        String idPost = mListData.get(position).getId_post();
        Log.i(TAG, "onItemClick: "+idPost);
        //TabDGiaoRecyclerview.setVisibility(View.INVISIBLE);
        framChat.setVisibility(View.VISIBLE);
        DetailOrderFragment detailFragment = new DetailOrderFragment();
        Bundle bundle = new Bundle();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        bundle.putString(idpostvalue,idPost); // use as per your need
        detailFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.frame_cart,detailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void sendInput(int position) {
        mListData.remove(position-1);
    }
}