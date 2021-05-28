package com.example.AmateurShipper.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.AmateurShipper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore mFireStore;

    ScrollView layout_account;
    Button back;
    TextView mEmail,mPhone;

    public String IdUser;

    public ManageAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageAccountFragment newInstance(String param1, String param2) {
        ManageAccountFragment fragment = new ManageAccountFragment();
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
        View view =  inflater.inflate(R.layout.fragment_manage_account, container, false);
        back = view.findViewById(R.id.btn_back);
        layout_account = view.findViewById(R.id.layout_manage_account);
        mEmail = view.findViewById(R.id.email);
        mPhone = view.findViewById(R.id.phone_number);

        mFireStore = FirebaseFirestore.getInstance();

        getManageAccount();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToSetting();
            }
        });


        return view;
    }

    public void getManageAccount(){
        mFireStore.collection("ProfileShipper").document(IdUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.getData();
                        mPhone.setText(document.get("phone").toString());
                        mEmail.setText(document.get("email").toString());
                    }
                }
            }
        });
    }

    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        IdUser = user.getUid();
    }
    public void backToSetting(){
        layout_account.setVisibility(View.GONE);
        SettingFragment tabSetting= new SettingFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.manage_frame,tabSetting);
        fragmentTransaction.commit();
    }
}