package com.example.nitsarut.mymap;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

public class admin_recrive extends AppCompatActivity implements View.OnClickListener {

    private ImageView adm_recrive;
    private ImageButton btnnone,btnacp;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_recrive);

        adm_recrive = (PhotoView)findViewById(R.id.adm_recrive);
        adm_recrive.setImageResource(getIntent().getIntExtra("img_id",00));


        adm_recrive = (PhotoView)findViewById(R.id.adm_recrive);
        btnnone = (ImageButton)findViewById(R.id.btnnone);
        btnacp = (ImageButton)findViewById(R.id.btnacp);

        btnacp.setOnClickListener(this);

        btnnone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder adb = new AlertDialog.Builder(admin_recrive.this);
                adb.setTitle("ยกเลิกการรับเรื่อง ?");
                adb.setMessage("คุณแน่ใจว่าต้องการกลับสู่หน้าเมนู");
                adb.setNegativeButton("ยกเลิก", null);
                adb.setPositiveButton("ตกลง", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(admin_recrive.this,"กลับสู่หน้าเมนู" ,
                                Toast.LENGTH_LONG).show();
                        Intent a = new Intent(admin_recrive.this,home_admin.class);
                        startActivity(a);
                        finish();
                    }
                });
                adb.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(admin_recrive.this,"รับเรื่องแล้ว",Toast.LENGTH_SHORT).show();
    }

    public void cancle(View view) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("กลับสู่หน้าเมนู?");
        adb.setMessage("คุณแน่ใจว่าต้องการกลับสู่หน้าเมนู");
        adb.setNegativeButton("ยกเลิก", null);
        adb.setPositiveButton("ตกลง", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(admin_recrive.this,"Your Click OK. " ,
                        Toast.LENGTH_LONG).show();
                Intent a = new Intent(admin_recrive.this,MainActivity.class);
                startActivity(a);
            }
        });
        adb.show();

    }
}
