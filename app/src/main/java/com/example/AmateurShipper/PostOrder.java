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
    EditText tennguoiguii, tennguoinhani, sdtnguoinhani, phuti, kmi, diemdii, diemdeni, tienungi, tienphii, ghichui;
    String tennguoigui, tennguoinhan, sdtnguoinhan, phut, km, diemdi, diemden, tienung, tienphi, ghichu;
    Button submit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_order);

        tennguoiguii = findViewById(R.id.editTextTenNguoiGui);
        tennguoinhani = findViewById(R.id.editTextTenNguoiNhan);
        sdtnguoinhani = findViewById(R.id.editTextTextSDTNGuoiNhan);
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
               // tao doi tuong
               PostOrderModel postOrder = new PostOrderModel(tennguoigui, tennguoinhan, sdtnguoinhan, phut, km, diemdi, diemden, tienung, tienphi, ghichu);

                //up data to firebase
               rootNode = FirebaseDatabase.getInstance();
               databaseReference = rootNode.getReference().child("newsfeed").child("userID1");
               databaseReference.setValue(postOrder);
           }
       });
    }
}