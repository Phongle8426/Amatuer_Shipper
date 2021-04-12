package com.example.AmateurShipper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#createInstance(String, String)} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView_chat;
    // TODO: Rename and change types of parameters
    public String id_post;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    public static final String MyPREFERENCESIDUSER = "MyPrefs";
    public static final String IDUSER = "iduser";
    SharedPreferences sharedpreferences;
    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param keyvalue Parameter 1.
     * @param id Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment createInstance(String keyvalue,String id) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        Log.d(TAG, "createInstance: keyvalue" + keyvalue);
        Log.d(TAG, "createInstance: keyvalue" + id);
        args.putString(keyvalue, id);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat,container,false);
        recyclerView_chat = view.findViewById(R.id.recycleview_mess);
        sharedpreferences = getActivity().getSharedPreferences(ReceivedOrderAdapter.MyPREFERENCES_IDPOST, Context.MODE_PRIVATE);
        loadDataIdPost();

        //rootNode = FirebaseDatabase.getInstance();
        //databaseReference = rootNode.getReference().child()
        return view;
    }

    public void loadDataIdPost(){
        id_post = sharedpreferences.getString(ReceivedOrderAdapter.IDPOST,"");
        Toast.makeText(getActivity(), "id post + " + id_post, Toast.LENGTH_SHORT).show();
    }
}