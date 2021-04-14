package com.example.AmateurShipper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.AmateurShipper.LoginActivity.IDUSER;
import static com.example.AmateurShipper.LoginActivity.MyPREFERENCESIDUSER;
import static com.example.AmateurShipper.ReceivedOrderAdapter.MyPREFERENCES_IDPOST;

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
    EditText edtMessage;
    // TODO: Rename and change types of parameters
     String id_post, id_shipper, id_chat_room, id_shop,content_message;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    ChatAdapter chatAdapter;
    List<MessageObject> messageObjects_chat;

    //ImageButton close_chat;
    ImageButton btn_send_message;
    SharedPreferences sharedpreferences, sharedpreferencesIdUser;

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

        btn_send_message = (ImageButton) view.findViewById(R.id.btnSendMess);
        edtMessage = view.findViewById(R.id.edtMessage);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES_IDPOST, Context.MODE_PRIVATE);
        sharedpreferencesIdUser = this.getActivity().getSharedPreferences(MyPREFERENCESIDUSER, Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        loadDataIdPost();
        loadData();
        readMessage();
        recyclerView_chat = view.findViewById(R.id.recycleview_mess);
        recyclerView_chat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView_chat.setLayoutManager(linearLayoutManager);


        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return view;
    }

    public void sendMessage(){
        content_message = edtMessage.getText().toString();
        MessageObject messageObject = new MessageObject(content_message,"shipper");
        databaseReference.child("Chatroom").child(id_chat_room).
                push().setValue(messageObject).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                edtMessage.setText(null);
            }
        });

       // Log.i(TAG, "sendMessage: "+id_chat_room);
    }

    public void loadDataIdPost() {
        id_chat_room = sharedpreferences.getString(ReceivedOrderAdapter.IDPOST, "");
        Toast.makeText(getActivity(), "id post + " + id_post, Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        id_shipper = sharedpreferencesIdUser.getString(IDUSER, "");
        Toast.makeText(getContext(), id_shipper, Toast.LENGTH_SHORT).show();
    }


    public void readMessage() {
        messageObjects_chat = new ArrayList<>();

        databaseReference.child("Chatroom").child(id_chat_room).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //messageObjects_chat.clear();
                MessageObject messageObject = snapshot.getValue(MessageObject.class);
                messageObjects_chat.add(messageObject);
                Log.i(TAG, "onChildAdded: "+ 1);
                //chatAdapter.addItem(chatAdapter.getItemCount()-1,messageObject);
                chatAdapter = new ChatAdapter(getContext(), messageObjects_chat);
                recyclerView_chat.setAdapter(chatAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}