package com.example.AmateurShipper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;
import com.example.AmateurShipper.Model.NotificationWebObject;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Helper.MyButtonClickListner;
import Helper.MySwipeHelper;



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
    public static final String idpostvalue = "123";
    public static final String idtabvalue = "tab";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView NewsRecyclerview;
    ShimmerFrameLayout layout_shimmer;
    FrameLayout framChat;
    MainActivity mainActivity;
    private DatabaseReference mDatabase;
    List<PostObject> mData = new ArrayList<>();
    ReceivedOrderAdapter receivedOrderAdapter;
    ChatFragment chatFragment;
    String iDUser;
    FragmentManager fragmentManager;
    final String[] reasonList = {"reason1","reason2"};

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
        framChat =view.findViewById(R.id.frag_container_detail);
        layout_shimmer = view.findViewById(R.id.shimmer_status);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getUid();
        mainActivity = (MainActivity) getActivity();
        mainActivity.setCountOrder(0);
        mainActivity.disableNotification();
        getListStatusReceived();
        loadshimer();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        NewsRecyclerview.setHasFixedSize(true);
        mLayoutManager.setStackFromEnd(true);
        NewsRecyclerview.setLayoutManager(mLayoutManager);
        MySwipeHelper mySwipeHelper = new MySwipeHelper(getActivity(), NewsRecyclerview , 200) {
            @Override
            public void instaniatMyButton(final RecyclerView.ViewHolder viewHolder, List<MyButton> buff) {
                buff.add(new MyButton("Hủy", 30, Color.parseColor("#DC143C"), new MyButtonClickListner(){
                    @Override
                    public void onClick(final int pos) {
                        showReasonDelete(pos,viewHolder);
                    }
                }, getContext()));
                buff.add(new MyButton("Nhận hàng",30, Color.parseColor("#FF4BB54F"), new MyButtonClickListner(){
                    @Override
                    public void onClick(int pos) {
                        pos = viewHolder.getAdapterPosition();
                        String idshop = mData.get(pos).id_shop;
                        String idpost = mData.get(pos).id_post;
                        PostObject post = mData.get(pos);
                        mData.remove(pos);
                        receivedOrderAdapter.notifyItemChanged(pos);
                        mDatabase.child("received_order_status").child(iDUser).child(idpost).child("status").setValue("1");
                    }
                }, getContext()));
            }
        };
        if(mData!=null){
            mData.clear();
        }

        receivedOrderAdapter = new ReceivedOrderAdapter(mData, tab_nhan.this,this, fm,this);
        NewsRecyclerview.setAdapter(receivedOrderAdapter);
        NewsRecyclerview.smoothScrollToPosition(0);
        return view;
    }


    public void deleteItem(int pos,String reason, RecyclerView.ViewHolder viewHolder){
        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();
        pos = viewHolder.getAdapterPosition();
        String idshop = mData.get(pos).id_shop;
        String idpost = mData.get(pos).id_post;
        mData.remove(pos);
        receivedOrderAdapter.notifyItemChanged(pos);
        NotificationWebObject noti = new NotificationWebObject(idpost,iDUser,"3",timestamp);
        mDatabase.child("received_order_status").child(iDUser).child(idpost).setValue(null);
        mDatabase.child("OrderStatus").child(idshop).child(idpost).child("status").setValue("3");
        mDatabase.child("OrderStatus").child(idshop).child(idpost).child("reason").setValue(reason);
        mDatabase.child("Notification").child(idshop).push().setValue(noti);
        mDatabase.child("Transaction").child(idpost).child("status").setValue("3");
    }
    public void showReasonDelete(int pos,RecyclerView.ViewHolder viewHolder){
        final String[] reason = new String[1];
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.titleReasonDelete);
            builder.setSingleChoiceItems(reasonList,0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    reason[0] = reasonList[which];

                }
            }).setPositiveButton("Đồng ý",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteItem(pos,reason[0],viewHolder);
                    dialogInterface.dismiss();
                }
            }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog mDialog = builder.create();
            mDialog.show();

    }

    public void getListStatusReceived(){
        mDatabase.child("received_order_status").child(iDUser).orderByChild("status").equalTo("0").addValueEventListener(new ValueEventListener() {
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
                    Toast.makeText(getContext(), "khong the load", Toast.LENGTH_LONG).show();
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
    @Override
    public void onItemClick(int position) {
        String idPost = mData.get(position).getId_post();
        //Log.i(TAG, "onItemClick: "+idPost);
        //NewsRecyclerview.setVisibility(View.INVISIBLE);
        framChat.setVisibility(View.VISIBLE);
        DetailOrderFragment detailFragment = new DetailOrderFragment();
        Bundle bundle = new Bundle();

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        bundle.putString(idpostvalue,idPost); // use as per your need
        bundle.putString(idtabvalue,"tabnhan");
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
                NewsRecyclerview.setVisibility(View.VISIBLE);
            }
        },1000);
    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onReceivedItem(int position){

    }

}