package com.example.AmateurShipper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class PostOrder extends AppCompatActivity {
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    EditText idshopi,tennguoiguii, tennguoinhani,sdtnguoiguii, sdtnguoinhani, phuti, kmi, diemdii, diemdeni, tienungi, tienphii, ghichui;
    String idshop,tennguoigui, tennguoinhan,sdtnguoigui, sdtnguoinhan, phut, diemdi, diemden, ghichu,km,tienung, tienphi;
    Button submit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_order);

        idshopi = findViewById(R.id.editTextIdShop);
        tennguoiguii = findViewById(R.id.editTextTenNguoiGui);
        tennguoinhani = findViewById(R.id.editTextTenNguoiNhan);
        sdtnguoinhani = findViewById(R.id.editTextTextSDTNGuoiNhan);
        sdtnguoiguii= findViewById(R.id.editTextSdtNguoiGui);
        phuti = findViewById(R.id.editTextTextPhut);
        kmi = findViewById(R.id.editTextTextKm);
        diemdii = findViewById(R.id.editTextTextDiemdi);
        diemdeni = findViewById(R.id.editTextTextDiemden);
        tienungi = findViewById(R.id.editTextTextTienUng);
        tienphii = findViewById(R.id.editTextTextTienPhi);
        ghichui  = findViewById(R.id.editTextTextGhiChu);
        submit_btn = findViewById(R.id.submit_btn);

       submit_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // anh xa
               tennguoigui = tennguoiguii.getText().toString();
               tennguoinhan = tennguoinhani.getText().toString();
               sdtnguoinhan = sdtnguoinhani.getText().toString();
               phut = phuti.getText().toString();
               km = kmi.getText().toString();
               diemdi = diemdii.getText().toString();
               diemden = diemdeni.getText().toString();
               tienung = tienungi.getText().toString();
               tienphi = tienphii.getText().toString();
               ghichu = ghichui.getText().toString();
               idshop=idshopi.getText().toString();
               sdtnguoigui = sdtnguoiguii.getText().toString();
               // tao doi tuong
               PostObject postOrder = new PostObject("tennguoigui","tennguoigui","tennguoigui","tennguoigui","tennguoigui", "tennguoigui","tennguoigui", "tennguoigui","tennguoigui","tennguoigui","tennguoigui", "tennguoigui","keyvalue2");

                //up data to firebase
               rootNode = FirebaseDatabase.getInstance();
               databaseReference = rootNode.getReference().child("nsf");
               databaseReference.child("keyvalue2").setValue(postOrder);

           }
       });
    }
}