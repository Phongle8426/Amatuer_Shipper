package com.example.AmateurShipper.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.AmateurShipper.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentServiceRules#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentServiceRules extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView readFile;
    ImageView back;
    ScrollView scrollView;
    public FragmentServiceRules() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentServiceRules.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentServiceRules newInstance(String param1, String param2) {
        FragmentServiceRules fragment = new FragmentServiceRules();
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
        ViewGroup root;
        View view = inflater.inflate(R.layout.fragment_service_rules, container, false);
        readFile = view.findViewById(R.id.read_file_doc);
        scrollView = view.findViewById(R.id.service_rules_fragment);
        back = view.findViewById(R.id.back_service_rules);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.setVisibility(View.GONE);
                SettingFragment  settingFragment = new SettingFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_service_rules,settingFragment);
                fragmentTransaction.commit();
            }
        });
        readUserData();
        return view;
    }

    public void readUserData() {
        String result = "";
        try {
            InputStream inputStream =  getActivity().getAssets().open("mytext.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            result = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        readFile.setText(result);
    }

}