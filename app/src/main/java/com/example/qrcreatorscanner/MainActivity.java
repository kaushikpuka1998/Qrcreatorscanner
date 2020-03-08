package com.example.qrcreatorscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

Thread thread =new Thread()
{
    @Override
    public void run()
    {
        try{
            sleep(3000);
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally
        {
            Intent g = new Intent(MainActivity.this,StartActivity.class);
            startActivity(g);
        }
    }
};
thread.start();
    }
}
