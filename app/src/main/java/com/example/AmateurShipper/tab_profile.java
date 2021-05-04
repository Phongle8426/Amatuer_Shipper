package com.example.AmateurShipper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.AmateurShipper.Model.ProfileObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;
//import static com.example.AmateurShipper.LoginActivity.IDUSER;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.example.AmateurShipper.LoginActivity.MyPREFERENCES;
//import static com.example.AmateurShipper.LoginActivity.MyPREFERENCESIDUSER;
import static com.example.AmateurShipper.LoginActivity.USERNAME;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab_profile extends Fragment implements PopupMenu.OnMenuItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Uri imageUri,idImageUri;
    private StorageReference mStorage;
    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;
    de.hdodenhof.circleimageview.CircleImageView avata;
    EditText name, email, address,phone;
    TextView cmnd;
    ImageButton img_cmnd;
    ImageView setting;
    Button update;
    RatingBar star;
    SharedPreferences sharedpreferences,sharedpreferencesIdUser;
    public String getname, getphone, getemail, getaddress, getUriAvatar, getUriCMND,getStar,iDUser;


    public tab_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static tab_profile newInstance(String param1, String param2) {
        tab_profile fragment = new tab_profile();
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
        View view = inflater.inflate(R.layout.fragment_tab_profile, container, false);
        name = view.findViewById(R.id.tv_name);
        email = view.findViewById(R.id.edt_email);
        phone = view.findViewById(R.id.edt_phone);
        address = view.findViewById(R.id.edt_address);
        cmnd = view.findViewById(R.id.edt_cmnd);
        img_cmnd = view.findViewById(R.id.btn_img_cmnd);
        update = view.findViewById(R.id.btn_update);
        star = view.findViewById(R.id.star_rate);
        avata = view.findViewById(R.id.img_poster);
        setting = view.findViewById(R.id.btn_setting);
        mStorage = FirebaseStorage.getInstance().getReference();
        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)
        });
      //  sharedpreferencesIdUser = this.getActivity().getSharedPreferences(MyPREFERENCESIDUSER, Context.MODE_PRIVATE);
        getUid();
        readProfile();
        avata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();

            }
        });

        img_cmnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseIDCardPicture();
            }
        });


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetting(v);
            }
        });

        return view;
    }

    //lấy UID
    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        iDUser = user.getUid();
    }


    // Update the profile
    public void updateProfile() {
        getname = name.getText().toString();
        getemail = email.getText().toString();
        getaddress = address.getText().toString();
        getphone = phone.getText().toString();
        getUriCMND = cmnd.getText().toString();
        if (!checkError()) { // kiem tra thong tin nhap
            if (imageUri != null) {
                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setTitle("Uploading Avatar.....");
                pd.show();
                final String ramdomKey = UUID.randomUUID().toString();
                final StorageReference riversRef = mStorage.child("images/" + ramdomKey);
                // up image avatar to Storage
                riversRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                //  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                pd.dismiss();
                                Toast.makeText(getContext(), "Avatar Uploaded", Toast.LENGTH_SHORT).show();
                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        getUriAvatar = uri.toString();

                                        // Update the profile
                                        ProfileObject profileObject = new ProfileObject(getname, getphone, getaddress, getemail, getUriAvatar, getUriCMND,"2");
                                        mFireStore.collection("ProfileShipper").document(iDUser)
                                                .set(profileObject)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getContext(), "Update Profile Successful", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Update Profile Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                pd.dismiss();
                                Toast.makeText(getContext(), "Failed to Upload", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                pd.setMessage("Percentage: " + (int) progress + "%");
                            }

                        });
            } else {
                ProfileObject profileObject1 = new ProfileObject(getname, getphone, getaddress, getemail, getUriAvatar, getUriCMND,"2");
                mFireStore.collection("ProfileShipper").document(iDUser)
                        .set(profileObject1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Update Profile Successful", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Update Profile Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else Toast.makeText(getContext(), "Thong tin chua du", Toast.LENGTH_SHORT).show();
    }

    // go to choose the image in device
    public void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void chooseIDCardPicture(){
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
            avata.setImageURI(imageUri);
        }else if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null){
            idImageUri = data.getData();
            final String ramdomKey = UUID.randomUUID().toString();
            final StorageReference riversRefIdCard = mStorage.child("imagesIDCard/" + ramdomKey);
            riversRefIdCard.putFile(idImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    riversRefIdCard.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            getUriCMND  = uri.toString();
                            cmnd.setText(getUriCMND);
                        }
                    });

                }
            });
        }
    }



    // read the profie
    public void readProfile() {
        Toast.makeText(getContext(), iDUser, Toast.LENGTH_SHORT).show();
        DocumentReference docRef = mFireStore.collection("ProfileShipper").document(iDUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.getData();
                        name.setText(document.get("name").toString());
                        address.setText(document.get("address").toString());
                        email.setText(document.get("email").toString());
                        phone.setText(document.get("phone").toString());
                        cmnd.setText(document.get("cmnd").toString());
                        //star.setNumStars(Integer.parseInt(document.get("rate_star").toString()));
                        if(!document.get("avatar").toString().equals(null)){
                            Glide.with(getContext()).load(document.get("avatar").toString()).into((de.hdodenhof.circleimageview.CircleImageView )avata);
                        }

                        getname=document.get("name").toString();
                        getaddress=document.get("address").toString();
                        getemail=document.get("email").toString();
                        getphone=document.get("phone").toString();
                        getUriCMND=document.get("cmnd").toString();
                        getUriAvatar=document.get("avatar").toString();
                    } else {
                        Toast.makeText(getContext(), "Load Profile Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Load Profile Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void sigOut(){
            mAuth.signOut();
            clearData();
            Intent intent_toLogin = new Intent(getActivity(),LoginActivity.class);
            intent_toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent_toLogin);
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }
    public void dialogLogOut(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Đăng Xuất!");
        dialog.setMessage("Bạn thực sự muốn đăng xuất?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sigOut();
            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog al = dialog.create();
        al.show();
    }
    public void openSetting(View v){
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.setting_menu);
        popupMenu.show();
    }
    // Hàm thực hiện các chức năng có trong setting
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.item1:
                dialogLogOut();
                return true;
            default: return false;
        }
    }
    public boolean checkError(){
        final String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String Email = email.getText().toString();
        String Name = name.getText().toString();
        String Phone = phone.getText().toString();
        String Address = address.getText().toString();
        String Cmnd = cmnd.getText().toString();

        if (Email.isEmpty() || Phone.isEmpty() || Name.isEmpty() || Address.isEmpty())
            return true;
        if (Phone.length() < 10)
            return true;
        if (!Email.matches(emailPattern))
            return true;
        return false;
    }
}