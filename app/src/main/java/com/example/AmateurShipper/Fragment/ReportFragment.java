package com.example.AmateurShipper.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AmateurShipper.Model.ReportObject;
import com.example.AmateurShipper.R;
import com.example.AmateurShipper.Util.formatTimeStampToDate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.AmateurShipper.Fragment.tab_nhan.idpostvalue;
import static com.example.AmateurShipper.Fragment.tab_nhan.idtabvalue;
import static com.example.AmateurShipper.Fragment.tab_nhan.idtshopvalue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CardView card1,card2,card3,card4,card5,card6,card7;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    ImageButton back;
    String id_cur_post,id_cur_shop,idUser;
    DatabaseReference mDatabase;
    private FirebaseFirestore mFireStore;

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
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
         View view = inflater.inflate(R.layout.fragment_report, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id_cur_post = bundle.getString(idpostvalue, "1");
            id_cur_shop = bundle.getString(idtshopvalue,"1");
        }
        card1 = view.findViewById(R.id.card_1);
        card2 = view.findViewById(R.id.card_2);
        card3 = view.findViewById(R.id.card_3);
        card4 = view.findViewById(R.id.card_4);
        card5= view.findViewById(R.id.card_5);
        card6 = view.findViewById(R.id.card_6);
        card7 = view.findViewById(R.id.card_7);
        tv1 = view.findViewById(R.id.tv_1);
        tv2 = view.findViewById(R.id.tv_2);
        tv3 = view.findViewById(R.id.tv_3);
        tv4 = view.findViewById(R.id.tv_4);
        tv5 = view.findViewById(R.id.tv_5);
        tv6 = view.findViewById(R.id.tv_6);
        tv7 = view.findViewById(R.id.tv_7);
        back = view.findViewById(R.id.btn_back);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFireStore = FirebaseFirestore.getInstance();

        getUId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTolist();
            }
        });
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report(tv1.getText().toString());
                backTolist();
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report(tv2.getText().toString());
                backTolist();
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report(tv3.getText().toString());
                backTolist();
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report(tv4.getText().toString());
                backTolist();
            }
        });
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report(tv5.getText().toString());
                backTolist();
            }
        });
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report(tv6.getText().toString());
                backTolist();
            }
        });
        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report(tv7.getText().toString());
                backTolist();
            }
        });
        return view;
    }

    public void report(String content){
        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();
        formatTimeStampToDate fm = new formatTimeStampToDate();
        String id_report = fm.convertTimeToId(tsLong);
        DocumentReference docRef = mFireStore.collection("ProfileShipper").document(idUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.getData();
                        String email = document.get("Email").toString();
                        String name = document.get("Name").toString ();
                        ReportObject report = new ReportObject(content,email,name,id_cur_post,
                                id_report,"0",timestamp,"0","1","");
                        mDatabase.child("report").child(idUser).child(id_report).setValue(report);
                        Toast.makeText(getContext(), "Cảm ơn báo cáo của bạn", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void getUId(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getUid();
    }
    public void backTolist(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        tab_nhan tabNhanFragment = new tab_nhan();
        fragmentTransaction.replace(R.id.report_frame,tabNhanFragment);
        fragmentTransaction.commit();
    }
}