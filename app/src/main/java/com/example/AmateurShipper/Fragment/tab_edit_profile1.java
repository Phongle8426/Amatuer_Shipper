package com.example.AmateurShipper.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.AmateurShipper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab_edit_profile1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab_edit_profile1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout layoutEditProfile;
    public Uri imageUri,idImageUri;
    private StorageReference mStorage;
    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;
    de.hdodenhof.circleimageview.CircleImageView avatar,cmnd;
    EditText address,name;
    TextView birthday,sex;
    Button editProfile;
    ImageButton back;
    public String idUser,birthday1,sexual1,fullname1,idcard1,address1,avatar1,getUriCMND="",getUriAvatar="";
    final String[] sexList = {"Nữ","Nam"};

    public tab_edit_profile1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab_edit_profile1.
     */
    // TODO: Rename and change types and number of parameters
    public static tab_edit_profile1 newInstance(String param1, String param2) {
        tab_edit_profile1 fragment = new tab_edit_profile1();
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
        View view = inflater.inflate(R.layout.fragment_tab_edit_profile1, container, false);
        avatar = view.findViewById(R.id.img_avatar);
        cmnd = view.findViewById(R.id.img_cmnd);
        address = view.findViewById(R.id.edt_address);
        name = view.findViewById(R.id.edt_name);
        birthday = view.findViewById(R.id.tv_birthday);
        sex = view.findViewById(R.id.tv_sexual);
        editProfile = view.findViewById(R.id.btn_update);
        back = view.findViewById(R.id.btn_back);
        layoutEditProfile = view.findViewById(R.id.layout_edit_profile);

        mStorage = FirebaseStorage.getInstance().getReference();
        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getUid();
        readProfile();

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBirthday();
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAvatar();
            }
        });
        cmnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIdCard();
            }
        });

        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectSex();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backProfile();
            }
        });
        return view;
    }

    public void editBirthday(){
        Long tsLong = System.currentTimeMillis()/1000;
        int selectedYear = 1999;
        int selectedMonth = 11;
        int selectedDayOfMonth = 22;
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Long ts;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
                try {
                     ts = dateFormat.parse(dayOfMonth+"/"+month+"/"+year).getTime()/1000;

                    if ((tsLong-ts) < 367993600){
                        birthday.setText("22/11/1999");
                    }else birthday.setText(dayOfMonth+"/"+month+"/"+year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
        datePickerDialog.show();
    }

    public void showSelectSex(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn giới tính");
        builder.setSingleChoiceItems(sexList,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sex.setText(sexList[which]);
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    public void readProfile(){
        DocumentReference docRef = mFireStore.collection("ProfileShipper").document(idUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.getData();
                        fullname1 = document.get("fullname").toString();
                        address1 = document.get("address").toString();
                        birthday1 = document.get("birthday").toString();
                        sexual1 = document.get("sexual").toString();
                        idcard1 = document.get("cmnd").toString();
                        avatar1 = document.get("avatar").toString();
                        name.setText(document.get("fullname").toString());
                        if (!address1.equals("")) address.setText(address1);
                        else address.setText(null);
                        if (!birthday1.equals("")) birthday.setText(birthday1);
                        else birthday.setText(null);
                        if (!sexual1.equals("")) sex.setText(sexual1);
                        else sex.setText(null);
                        if(!document.get("cmnd").toString().equals("")){
                            Glide.with(getContext()).load(document.get("CMND").toString()).into((de.hdodenhof.circleimageview.CircleImageView )cmnd);
                        }
                        if(!document.get("avatar").toString().equals(null)){
                            Glide.with(getContext()).load(document.get("avatar").toString()).into((de.hdodenhof.circleimageview.CircleImageView )avatar);
                        }

                        editProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!getUriAvatar.equals("")){
                                    Log.i(TAG, "onComplete: "+ getUriAvatar);
                                    avatar1=getUriAvatar;
                                }
                                if (!getUriCMND.equals("")){
                                    idcard1=getUriCMND;
                                }
                                updateProfile(fullname1,address1,birthday1,sexual1,idcard1,avatar1);
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "Load Profile Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateProfile(String fullname2, String address2, String birthday2, String sexual2, String idcard2, String avatar2){

        if (!name.getText().toString().equals(""))
                fullname2 = name.getText().toString().trim();
        if (!address.getText().toString().equals(""))
                address2 = address.getText().toString().trim();
        birthday2 = birthday.getText().toString();
        sexual2 = sex.getText().toString();

        DocumentReference docRef = mFireStore.collection("ProfileShipper").document(idUser);
        Map<String, Object> map = new HashMap<>();
        map.put("fullname",fullname2);
        map.put("address",address2);
        map.put("birthday",birthday2);
        map.put("sexual",sexual2);
        map.put("cmnd",idcard2);
        map.put("avatar",avatar2);
         docRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Toast.makeText(getContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(getContext(), "That bai", Toast.LENGTH_SHORT).show();
             }
         });
    }

    public void editAvatar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void editIdCard() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            avatar.setImageURI(imageUri);
            final String ramdomKey = UUID.randomUUID().toString();
            final StorageReference riversRefIdCard = mStorage.child("images/" + ramdomKey);
            riversRefIdCard.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    riversRefIdCard.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            getUriAvatar = uri.toString();
                        }
                    });

                }
            });

        }else if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null){
            idImageUri = data.getData();
            cmnd.setImageURI(idImageUri);
            final String ramdomKey = UUID.randomUUID().toString();
            final StorageReference riversRefIdCard = mStorage.child("imagesIDCard/" + ramdomKey);
            riversRefIdCard.putFile(idImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    riversRefIdCard.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            getUriCMND  = uri.toString();
                        }
                    });

                }
            });
        }
    }

    public void backProfile(){
        layoutEditProfile.setVisibility(View.GONE);
        EditProfileFragment profile = new EditProfileFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.edit_profile_frame,profile);
        fragmentTransaction.commit();
    }
    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getUid();
    }
}