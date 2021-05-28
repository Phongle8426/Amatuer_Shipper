package com.example.AmateurShipper.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.content.ContentValues.TAG;
import static com.example.AmateurShipper.Fragment.tab_nhan.idpostvalue;
import static com.example.AmateurShipper.Fragment.tab_nhan.idtabvalue;
import static com.example.AmateurShipper.Fragment.tab_nhan.idpostvalue;
import static com.example.AmateurShipper.Fragment.tab_nhan.idtabvalue;
import static com.example.AmateurShipper.Fragment.tab_nhan.idtshopvalue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailOrderFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback,PopupMenu.OnMenuItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CALL = 1;
    public static final String id_room = "123";
    public static final String ten_shop = "Huynh Ba Thang";
    public static final String ID_POST = "123";
    public static final String ID_SHOP = "123";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ShimmerFrameLayout layout_shimmer;
    RelativeLayout frame_shimmer;
    private DatabaseReference mDatabase;
    String iDUser,sdt_nguoi_nhan_hang,sdt_shop,idRoom,fromTab;
    ImageView callShop,callCustomer,message_shop,back;
    ImageButton report;
    TextView tng,sdtnguoigui,noinhan,noigiao,sdtnguoinhan,tennguoinhan,ghichu,thoigian,phigiao,phiung,sokm,tv_id_post,id_post2;
    String ten_nguoi_gui,sdt_nguoi_gui,noi_nhan,noi_giao,sdt_nguoi_nhan,ten_nguoi_nhan,ghi_chu,thoi_gian,
            id_shop,phi_giao,phi_ung,km,id_cur_shop,id_cur_post;
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
            id_cur_shop = bundle.getString(idtshopvalue,"1");
            Toast.makeText(getContext(), id_cur_shop, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "BBBBBBBBB: "+ id_cur_shop +"/"+ id_cur_post);

            fromTab = bundle.getString(idtabvalue,"tab");
        }
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_detail_order, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        layout_shimmer = view.findViewById(R.id.shimmer_detail_order);
        frame_shimmer = view.findViewById(R.id.frame_shimmer);
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
        report= view.findViewById(R.id.report);
                     //sokm = view.findViewById(R.id.editTextTextKm);
                   // close_btn = myview_dia.findViewById(R.id.close_chat);

        getUid();
        getDataDetail();
        loadshimer();
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
                closeDetail();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReport(view);
            }
        });
        return view;
    }


    public void getDataDetail(){
        mDatabase.child("received_order_status").child(iDUser).child(id_cur_post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    PostObject data = snapshot.getValue(PostObject.class);
                    if (data.getStatus().equals("2")) {
                        message_shop.setVisibility(View.GONE);
                        callCustomer.setVisibility(View.GONE);
                        callShop.setVisibility(View.GONE);
                        sdtnguoinhan.setVisibility(View.GONE);
                        sdtnguoigui.setVisibility(View.GONE);
                    }
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
                    ten_nguoi_gui = data.getTen_nguoi_gui();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void openReport(View v){
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.reoport_menu);
        popupMenu.show();
    }
    // Hàm thực hiện các chức năng có trong setting
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.report:
                    goToReport();
                return true;
            default: return false;
        }
    }

    public void goToReport(){
        ReportFragment reportFragment = new ReportFragment();
        Bundle bundle = new Bundle();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        bundle.putString(ID_POST,id_cur_post); // use as per your need
        bundle.putString(ID_SHOP,id_cur_shop);
        reportFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frame_cart,reportFragment);
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
                frame_shimmer.setVisibility(View.VISIBLE);;
            }
        },500);
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
        String number_shop = sdt_shop;
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
        String number_customer = sdt_nguoi_nhan_hang;
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
        mDatabase.child("Transaction").child(id_cur_post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idRoom = snapshot.child("id_roomchat").getValue(String.class);
                ChatFragment chatFragment = new ChatFragment();
                Bundle bundle = new Bundle();
               // Log.i(TAG, "openMessageFragment: "+ idRoom);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_from_bottom,R.anim.slide_down_top);
                bundle.putString(id_room,idRoom); // use as per your need
                bundle.putString(ten_shop,ten_nguoi_gui);
                chatFragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.add(R.id.frame_cart,chatFragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void closeDetail(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        if (fromTab.equals("tabnhan")){
            tab_nhan tabNhanFragment = new tab_nhan();
            fragmentTransaction.replace(R.id.frame_cart,tabNhanFragment);
        }else if(fromTab.equals("tabdanggiao")){
            tab_dang_giao tabDangGiaoFragment = new tab_dang_giao();
            fragmentTransaction.replace(R.id.frame_cart,tabDangGiaoFragment);
        }else if(fromTab.equals("tablichsu")){
            tab_lich_su tabLichSuFragment = new tab_lich_su();
            fragmentTransaction.replace(R.id.frame_cart,tabLichSuFragment);
        }
        fragmentTransaction.commit();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.remove(this).commitAllowingStateLoss();
    }
    //Load ID User
    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        iDUser = user.getUid();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated: chay");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: chay");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: chay");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause: chay");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop: chay");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView: chay");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: chay");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}