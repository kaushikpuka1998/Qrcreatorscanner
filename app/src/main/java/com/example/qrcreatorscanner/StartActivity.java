package com.example.qrcreatorscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.GeneratedAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    Button scanbutton;
    Button GenerateButton;
    Button Scannedhistorybutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        scanbutton = findViewById(R.id.scanbutton);
        Scannedhistorybutton = findViewById(R.id.scannedhistorybutton);
        GenerateButton = findViewById(R.id.generatebutton);


        Scannedhistorybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(StartActivity.this,ScannedResultActivity.class);
                startActivity(f);
            }
        });

        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(StartActivity.this,scActivity.class);
                startActivity(n);
            }
        });

        GenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent y = new Intent(StartActivity.this,genActivity.class);
                startActivity(y);
            }
        });


    }
    boolean Backpressed = false;

    @Override
    public void onBackPressed() {
        if(Backpressed)
        {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }else
        {
            Backpressed = true;
            Toast.makeText(this, "Press again to Exit", Toast.LENGTH_SHORT).show();

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Backpressed = false;

                }
            },1000);
        }

    }
}
