package com.example.AmateurShipper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
//import static com.example.AmateurShipper.LoginActivity.IDUSER;
//import static com.example.AmateurShipper.LoginActivity.MyPREFERENCESIDUSER;
import static com.example.AmateurShipper.DetailOrderFragment.id_room;
import static com.example.AmateurShipper.ReceivedOrderAdapter.MyPREFERENCES_IDPOST;
import static com.example.AmateurShipper.tab_nhan.idpostvalue;

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
     String id_post, id_shipper, id_chat_room, id_shop,content_message,getUriChatMess;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    private StorageReference mStorage;
    ChatAdapter chatAdapter;
    List<MessageObject> messageObjects_chat;
    Uri imageUri;
    //ImageButton close_chat;
    ImageButton btn_send_message,btn_send_image;
    SharedPreferences sharedpreferences;
    ValueEventListener seenMessage;
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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id_chat_room = bundle.getString(id_room, "1");
        }
        btn_send_message = (ImageButton) view.findViewById(R.id.btnSendMess);
        btn_send_image = view.findViewById(R.id.btn_mess_picture);
        edtMessage = view.findViewById(R.id.edtMessage);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES_IDPOST, Context.MODE_PRIVATE);
       // sharedpreferencesIdUser = this.getActivity().getSharedPreferences(MyPREFERENCESIDUSER, Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
       // loadDataIdPost();
        getUid();
        readMessage();
        recyclerView_chat = view.findViewById(R.id.recycleview_mess);
        recyclerView_chat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView_chat.setLayoutManager(linearLayoutManager);

        btn_send_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_message = edtMessage.getText().toString();
                sendMessage(content_message,"");
            }
        });
        seenMessage();
        return view;
    }

    public String getTimeMessage(){
        DateFormat df = new SimpleDateFormat("HH':'mm");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
           // avata.setImageURI(imageUri);
            sendImageMessage();
        }
    }

    public void seenMessage(){
        seenMessage = databaseReference.child("Chatroom").child(id_chat_room).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        MessageObject mess = dataSnapshot.getValue(MessageObject.class);
                        if (!mess.getId().equals(id_shipper)){
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("isseen","1");
                            snapshot.getRef().child(dataSnapshot.getKey()).updateChildren(hashMap);
                        }
                    }
                }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void sendImageMessage(){
        if(imageUri != null){
            final String ramdomKey = UUID.randomUUID().toString();
            final StorageReference riversRef = mStorage.child("imagesChat/" + ramdomKey);
            riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            getUriChatMess = uri.toString();
                            sendMessage("",getUriChatMess);
                        }
                    });
                }
            });
        }
    }

    public void sendMessage(String content_message,String content_image){
        if (content_message != null){
        MessageObject messageObject = new MessageObject(content_message,id_shipper,content_image,"0",getTimeMessage(),"Aron WanbiSaka");
        databaseReference.child("Chatroom").child(id_chat_room).
                push().setValue(messageObject).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                edtMessage.setText(null);
            }
        });
        }

       // Log.i(TAG, "sendMessage: "+id_chat_room);
    }

    public void loadDataIdPost() {
        id_chat_room = sharedpreferences.getString(ReceivedOrderAdapter.IDPOST, "");
        Toast.makeText(getActivity(), "id post + " + id_post, Toast.LENGTH_SHORT).show();
    }

    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        id_shipper = user.getUid();
    }


    public void readMessage() {
        messageObjects_chat = new ArrayList<>();
        databaseReference.child("Chatroom").child(id_chat_room).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageObjects_chat.clear();
                if (snapshot.exists()){
                    for (DataSnapshot data : snapshot.getChildren()){
                        MessageObject messageObject = data.getValue(MessageObject.class);
                        messageObjects_chat.add(messageObject);
                        //chatAdapter.addItem(chatAdapter.getItemCount()-1,messageObject);
                        chatAdapter = new ChatAdapter(getContext(), messageObjects_chat);
                        recyclerView_chat.setAdapter(chatAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenMessage);
    }
}