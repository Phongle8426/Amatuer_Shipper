package com.example.AmateurShipper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;
import static com.example.AmateurShipper.tab_nhan.idpostvalue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailOrderFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CALL = 1;
    public static final String id_room = "123";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabase;
    String iDUser,sdt_nguoi_nhan_hang,sdt_shop,idRoom;
    ImageView callShop,callCustomer,message_shop,back;
    TextView tng,sdtnguoigui,noinhan,noigiao,sdtnguoinhan,tennguoinhan,ghichu,thoigian,phigiao,phiung,sokm,tv_id_post,id_post2;
    String ten_nguoi_gui,sdt_nguoi_gui,noi_nhan,noi_giao,sdt_nguoi_nhan,ten_nguoi_nhan,ghi_chu,thoi_gian,
            id_shop,phi_giao,phi_ung,km,id_post,id_cur_post;
    public DetailOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailOrderFragment newInstance(String param1, String param2) {
        DetailOrderFragment fragment = new DetailOrderFragment();
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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id_cur_post = bundle.getString(idpostvalue, "1");
        }
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.dialogcontent, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        callCustomer = view.findViewById(R.id.btn_customer_phone_number);
        callShop =view.findViewById(R.id.btn_shop);
        message_shop = view.findViewById(R.id.btn_massage);
         tng = view.findViewById(R.id.tv_ten_nguoi_gui);
         sdtnguoigui = view.findViewById(R.id.editTextSoDTNguoiGui);
         noinhan = view.findViewById(R.id.editTextTextDiemdi);
         noigiao = view.findViewById(R.id.editTextTextDiemden);
         sdtnguoinhan = view.findViewById(R.id.tv_sdt_nguoi_nhan);
         tennguoinhan = view.findViewById(R.id.tv_ten_nguoi_nhan);
         ghichu = view.findViewById(R.id.editTextTextGhiChu);
         thoigian = view.findViewById(R.id.editTextTextPhut);
         phigiao = view.findViewById(R.id.editTextTextTienPhi);
         phiung = view.findViewById(R.id.editTextTextTienUng);
         tv_id_post = view.findViewById(R.id.tv_id_post);
        id_post2 = view.findViewById(R.id.id_post);
        back = view.findViewById(R.id.btn_back);
                     //sokm = view.findViewById(R.id.editTextTextKm);
                   // close_btn = myview_dia.findViewById(R.id.close_chat);

        getUid();
        getDataDetail();
        callShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_shop();
            }
        });
        callCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_customer();
            }
        });
        message_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMessageFragment();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }


    public void getDataDetail(){
        mDatabase.child("received_order_status").child(iDUser).child(id_cur_post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostObject data = snapshot.getValue(PostObject.class);
                    tng.setText(data.getTen_nguoi_gui());
                    sdtnguoigui.setText(data.getSdt_nguoi_gui());
                    noinhan.setText(data.getNoi_nhan());
                    noigiao.setText(data.getNoi_giao());
                    sdtnguoinhan.setText(data.getSdt_nguoi_nhan());
                    tennguoinhan.setText(data.getTen_nguoi_nhan());
                    ghichu.setText(data.getGhi_chu());
                    thoigian.setText(data.getThoi_gian());
                    phigiao.setText(data.getPhi_giao());
                    phiung.setText(data.getPhi_ung());
                    tv_id_post.setText(data.getId_post());
                    id_post2.setText(data.getId_post());
                    //sokm.setText(data.getKm());
                    sdt_nguoi_nhan_hang = data.getSdt_nguoi_nhan();
                    sdt_shop = data.getSdt_nguoi_gui();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getIdRoom(){
        mDatabase.child("Transaction").child(id_cur_post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idRoom = snapshot.child("id_roomchat").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call_customer();
                call_shop();
            } else {
                Toast.makeText(getActivity(), "permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void call_shop() {
        String number_shop = "0" + sdt_shop;
        if (number_shop.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            else {
                String dial = "tel: " + number_shop;
                getActivity().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    private void call_customer() {
        String number_customer = "0" + sdt_nguoi_nhan_hang;
        if (number_customer.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            else {
                String dial = "tel: " + number_customer;
                getActivity().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    public void openMessageFragment(){
        getIdRoom();
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        bundle.putString(id_room,idRoom); // use as per your need
        chatFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frag_container_detail,chatFragment);
        fragmentTransaction.commit();
    }
    //Load ID User
    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        iDUser = user.getUid();
    }

}