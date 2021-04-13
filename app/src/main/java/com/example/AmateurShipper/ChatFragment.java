package com.example.AmateurShipper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.Server;

import static android.content.ContentValues.TAG;
import static com.example.AmateurShipper.LoginActivity.MyPREFERENCES;

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
    public String id_post, id_user, id_chat_room, id_shop;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    ChatAdapter chatAdapter;
    List<MessageObject> messageObjects_chat;

    //ImageButton close_chat;
    ImageButton btn_send_message;


    SharedPreferences sharedpreferences, sharedPreferences_shipper_id, sharedPreferences_id_shop;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param keyvalue Parameter 1.
     * @param id       Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment createInstance(String keyvalue, String id) {
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
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView_chat = view.findViewById(R.id.recycleview_mess);
        recyclerView_chat.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView_chat.setLayoutManager(linearLayoutManager);
        sharedpreferences = getActivity().getSharedPreferences(ReceivedOrderAdapter.MyPREFERENCES_IDPOST, Context.MODE_PRIVATE);
        sharedPreferences_shipper_id = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences_id_shop = this.getActivity().getSharedPreferences(ReceivedOrderAdapter.IDSHOP, Context.MODE_PRIVATE);
        chatAdapter = new ChatAdapter(getContext(), messageObjects_chat);
        recyclerView_chat.setAdapter(chatAdapter);
        loadDataIdPost();
        loadIDSHOP();
        loadIdUser();

        btn_send_message = (ImageButton) view.findViewById(R.id.btnSendMess);

       databaseReference.child("Transaction").child(id_post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    id_chat_room = snapshot.child("id_roomchat").getValue(String.class);
                    Toast.makeText(getActivity(), "id rooom" + id_chat_room, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Data snap is not exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        readMessage("9ebbP1pQi1JCcF1vkGfN");
        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageObject messageObject = new MessageObject("hello world", id_user);
              databaseReference.child("Chatroom").child(id_chat_room).
                      push().setValue(messageObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
            }
        });
        return view;
    }

    public void loadDataIdPost() {
        id_post = sharedpreferences.getString(ReceivedOrderAdapter.IDPOST, "");
        Toast.makeText(getActivity(), "id post + " + id_post, Toast.LENGTH_SHORT).show();
    }

    public void loadIdUser() {
        id_user = sharedPreferences_shipper_id.getString(LoginActivity.IDUSER, "");
    }

    public void loadIDSHOP() {
        id_shop = sharedPreferences_id_shop.getString(ReceivedOrderAdapter.IDSHOP, "");
        Log.i(TAG, "loadIDSHOP: " + id_shop);
    }

    public void readMessage(String idroom) {
        messageObjects_chat = new ArrayList<>();
        databaseReference.child("Chatroom").child(idroom)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageObjects_chat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageObject messageObject = dataSnapshot.getValue(MessageObject.class);
                    messageObjects_chat.add(messageObject);
                }
                chatAdapter = new ChatAdapter(getActivity(), messageObjects_chat);
                recyclerView_chat.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}