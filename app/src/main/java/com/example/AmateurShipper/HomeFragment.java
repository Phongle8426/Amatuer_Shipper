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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AmateurShipper.Dialog.FilterPaymentDialog;
import com.example.AmateurShipper.Util.PostDiffUtilCallback;
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
public class HomeFragment extends Fragment implements PostAdapter.OnPostListener,FilterPaymentDialog.OnInputSelected {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView NewsRecyclerview;
    PostAdapter postAdapter;
    SharedPreferences sharedPreferences;
   private List<PostObject> mData = new ArrayList<>();
    com.getbase.floatingactionbutton.FloatingActionButton btn_filter_location,btn_filter_payment;
    private DatabaseReference mDatabase;
    public int filter_payment = 0;
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
        btn_filter_location = view.findViewById(R.id.btn_filter_location);
        btn_filter_payment = view.findViewById(R.id.btn_filter_payment);
        NewsRecyclerview = view.findViewById(R.id.rcv_post);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        NewsRecyclerview.setHasFixedSize(true);
        mLayoutManager.setStackFromEnd(true);
        NewsRecyclerview.setLayoutManager(mLayoutManager);
        getList();
        postAdapter = new PostAdapter(mData, getContext(), this);
        NewsRecyclerview.setAdapter(postAdapter);
        NewsRecyclerview.smoothScrollToPosition(0);
        btn_filter_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        return view;
    }



    public void getList(){
        mDatabase.child("newsfeed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(getContext(), "ok", Toast.LENGTH_LONG).show();
                    List<PostObject> insertList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        PostObject data = dataSnapshot.getValue(PostObject.class);
                        if(filter_payment !=0){
                            if(filter_payment >= Integer.parseInt(data.getPhi_ung())){
                                insertList.add(data);
                            }
                        }else insertList.add(data);
                        //Toast.makeText(getContext(), "ten guoi gui" + data.getTen_nguoi_gui(), Toast.LENGTH_SHORT).show();


                    }
                    postAdapter.insertData(insertList);
                }else{
                    Toast.makeText(getContext(), "khong the load", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void showDialog(){
        final int countItem = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Lọc theo vị trí");
        final ArrayList<Integer> mLocationItem = new ArrayList<>();
        final String[] location = {"Hai Chau","Thanh Khe","Cam Le","Hoa Khanh"};
        final boolean[] checkedItems = new boolean[location.length];

        builder.setMultiChoiceItems(location, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean ischecked) {
                if (ischecked){
                    if (countItem <= 3){
                        if (!mLocationItem.contains(position)){
                            mLocationItem.add(position);
                        }
                    }
                }else mLocationItem.remove(position);
            }
        }).setPositiveButton("Lọc", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String item ="";
                for (int a = 0; a< mLocationItem.size() ;a++){
                        item = item + location[mLocationItem.get(a)] + ",";
                }
                Toast.makeText(getContext(), item, Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    @Override
    public void onPostClick(int position) {
        mData.get(position);
        getList();

    }

    @Override
    public void sendInput(String dialog_payment) {
        getList();
        filter_payment = Integer.parseInt(dialog_payment);
    }
}