package com.example.AmateurShipper.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.AmateurShipper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout layoutProfile;
    DatabaseReference mDatabase;
    private StorageReference mStorage;
    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;
    de.hdodenhof.circleimageview.CircleImageView avata;
    TextView name, email, address,phone,birthday,sex,level,propressLevel;
    Button editProfile;
    ImageView setting;
    public String idUser;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        name = view.findViewById(R.id.tv_name);
        email = view.findViewById(R.id.tv_email);
        phone = view.findViewById(R.id.tv_phone);
        address = view.findViewById(R.id.tv_address);
        birthday = view.findViewById(R.id.tv_birthday);
        sex = view.findViewById(R.id.tv_sex);
        avata = view.findViewById(R.id.img_poster);
        editProfile = view.findViewById(R.id.btn_edit_profile);
        setting = view.findViewById(R.id.btn_setting);
        level = view.findViewById(R.id.level_now);
        propressLevel = view.findViewById(R.id.progess);
        layoutProfile = view.findViewById(R.id.layout_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getUid();
        loadLevel();
        readProfile();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetting();
            }
        });
        return view;
    }

    public void loadLevel(){
        mDatabase.child("Ratting_Star").child(idUser).addValueEventListener(new ValueEventListener() {
            int sumPoint =0;
            int mlevel;
            String propress;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snap : snapshot.getChildren()){
                        if (snap.child("rate").getValue(Integer.class)==5)
                            sumPoint+=3;
                        else if (snap.child("rate").getValue(Integer.class)==4)
                            sumPoint+=2;
                        else if (snap.child("rate").getValue(Integer.class)==3)
                            sumPoint+=1;
                        else if (snap.child("rate").getValue(Integer.class)==2)
                            sumPoint-=2;
                        else if (snap.child("rate").getValue(Integer.class)==1)
                            sumPoint-=3;
                    }

                    if (sumPoint < 50){
                        mlevel =0;
                        propress=sumPoint+"/50";
                    }
                    if (sumPoint >= 50 && sumPoint < 100){
                        mlevel =1;
                        propress=sumPoint+"/100";
                    }
                    if (sumPoint >= 100 && sumPoint < 150){
                        mlevel =2;
                        propress=sumPoint+"/150";
                    }
                    if (sumPoint >= 150){
                        mlevel =3;
                        propress=sumPoint+"/150";
                    }
                    level.setText(mlevel+"");
                    propressLevel.setText(propress+"");
                }else{
                     level.setText("0");
                     propressLevel.setText("0/50");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void openSetting(){
        layoutProfile.setVisibility(View.GONE);
        SettingFragment tabSetting = new SettingFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.profile_frame,tabSetting);
        fragmentTransaction.commit();
    }

    public void editProfile(){
        layoutProfile.setVisibility(View.GONE);
        tab_edit_profile1 tabEditProfile1 = new tab_edit_profile1();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.profile_frame,tabEditProfile1);
        fragmentTransaction.commit();
    }
    public void readProfile() {
        DocumentReference docRef = mFireStore.collection("ProfileShipper").document(idUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.getData();
                        name.setText(document.get("fullname").toString());
                        address.setText(document.get("address").toString());
                        email.setText(document.get("email").toString());
                        phone.setText(document.get("phone").toString());
                        if(!document.get("avatar").toString().equals(null)){
                            Glide.with(getContext()).load(document.get("avatar").toString()).into((de.hdodenhof.circleimageview.CircleImageView )avata);
                        }

                    } else {
                        Toast.makeText(getContext(), "Load Profile Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getUid();
    }
}