package com.example.qrcreatorscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class genActivity extends AppCompatActivity {


    GridLayout all;
    TextView ContactID ,number;
    String z;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen);
        all = findViewById(R.id.all);
        ContactID = findViewById(R.id.nmae);
        number = findViewById(R.id.number);
        singleEvent(all);

    }
    private void singleEvent(GridLayout all)
    {
        for(int i =0;i<all.getChildCount();i++)
        {
            final int finalI = i;
            CardView cardView = (CardView)all.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalI == 0)
                    {
                        Toast.makeText(genActivity.this, "Text", Toast.LENGTH_SHORT).show();
                        Intent y = new Intent(genActivity.this,QRgenerationActivity.class);
                        startActivity(y);
                    }
                    else   if(finalI == 1)
                    {
                        Toast.makeText(genActivity.this, "Email", Toast.LENGTH_SHORT).show();
                        Intent y = new Intent(genActivity.this,EmailQRgenerateActivity.class);
                        startActivity(y);
                    }else   if(finalI == 2)
                    {
                        Toast.makeText(genActivity.this, "Web", Toast.LENGTH_SHORT).show();
                        Intent y = new Intent(genActivity.this,WebQRActivity.class);
                        startActivity(y);

                    }
                    else   if(finalI == 3)
                    {
                        Toast.makeText(genActivity.this, "Product Code", Toast.LENGTH_SHORT).show();
                        Intent y = new Intent(genActivity.this,ProductQRActivity.class);
                        startActivity(y);
                    }
                    else   if(finalI == 4)
                    {
                        Intent b = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

                        startActivityForResult(b,7);
                        EnableRunTimePermission();


                    }else   if(finalI == 5)
                    {

                    }
                }
            });

        }

    }
    public void EnableRunTimePermission()
    {
            if(ActivityCompat.shouldShowRequestPermissionRationale(genActivity.this, Manifest.permission.READ_CONTACTS))
            {
                Toast.makeText(this, "Permission Granted for Contacts", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(genActivity.this,new String[]{
                        Manifest.permission.READ_CONTACTS
                },1);
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission Granted and Contact Received", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case(7):
                if(resultCode == Activity.RESULT_OK)
                {
                    Uri uri;
                    Cursor curso1,cursor2;
                    String TempNameHolder,TempNumberHolder,TempcontactID,IDResult = "";
                    int IDResultHolder;
                    uri = data.getData();
                    curso1 = getContentResolver().query(uri,null,null,null);
                    if(curso1.moveToFirst())
                    {
                        TempNameHolder = curso1.getString(curso1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        TempcontactID = curso1.getString(curso1.getColumnIndex(ContactsContract.Contacts._ID));
                       IDResult =  curso1.getString(curso1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                       IDResultHolder = Integer.valueOf(IDResult);

                       if(IDResultHolder == 1)
                       {
                           cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+TempcontactID,null,null );
                           while(cursor2.moveToNext())
                           {
                               TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                             String l =TempNameHolder ;
                             String y=  TempNumberHolder;

                             if(TextUtils.isEmpty(l) &  TextUtils.isEmpty(y))
                             {
                                 Toast.makeText(this, "Contact name or Number is absent", Toast.LENGTH_SHORT).show();
                             }else if( TextUtils.isEmpty(y))
                             {
                                 Intent d = new Intent(genActivity.this,ContactFieldActivity.class);
                                 d.putExtra("number",l+"\n");
                                 startActivity(d);
                             }
                             {
                                 Intent d = new Intent(genActivity.this,ContactFieldActivity.class);
                                 d.putExtra("number",l+"\n"+y);
                                 startActivity(d);

                             }

                           }
                       }

                    }

                }
                break;




        }
    }
}

