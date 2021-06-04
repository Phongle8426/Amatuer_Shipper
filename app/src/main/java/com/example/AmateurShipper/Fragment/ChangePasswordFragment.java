package com.example.AmateurShipper.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.AmateurShipper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageButton back;
    EditText email;
    br.com.simplepass.loading_button_lib.customViews.CircularProgressButton send;
    LinearLayout changePassLayout;
    FrameLayout changePassFrame;
    private FirebaseAuth mAuth;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        back = view.findViewById(R.id.btn_back);
        email = view.findViewById(R.id.editTextEmail);
        send = view.findViewById(R.id.sendButton);
        changePassFrame = view.findViewById(R.id.change_password_frame);
        changePassLayout = view.findViewById(R.id.change_password_layout);

        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToManage();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emaill = email.getText().toString();
                if (validateEmail(emaill))
                    sendLinkToChangePassword(emaill);
            }
        });
        return view;
    }
    public void sendLinkToChangePassword(String emailAdress){
        mAuth.sendPasswordResetEmail(emailAdress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(getContext(), "Thành công, hãy kiểm tra hộp thư của bạn.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(), "Không thể gửi.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateEmail(String iEmail) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (iEmail.isEmpty()) {
            email.setError("Không thể để trống");
            return false;
        } else if (!iEmail.matches(emailPattern)) {
            email.setError("Chưa đúng định dạng");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
    public void backToManage(){
        changePassLayout.setVisibility(View.GONE);
        ManageAccountFragment tabManage= new ManageAccountFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.change_password_frame,tabManage);
        fragmentTransaction.commit();
    }

}