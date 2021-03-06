package com.example.qrcreatorscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class QRgenerationActivity extends AppCompatActivity {

    FloatingActionButton floatbutton;
    ImageView imageView;
    EditText editText;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    StorageReference mStorageRef;
    Uri imguri;
    DatabaseReference storageReference = firebaseDatabase.getReference("Images");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_rgeneration);

        imageView = findViewById(R.id.imageView);
        floatbutton = findViewById(R.id.floatbutton);
        editText = findViewById(R.id.edittextqrgenerator);
        mStorageRef = FirebaseStorage.getInstance().getReference("GeneratedQR");



        floatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String text = editText.getText().toString();


                Intent a = new Intent(QRgenerationActivity.this,afterreadyqrActivity.class);
                if(!TextUtils.isEmpty(text))
                {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(text,BarcodeFormat.QR_CODE,300,300);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageView.setImageBitmap(bitmap);

                        ByteArrayOutputStream bs = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,50,bs);
                        byte[] data = bs.toByteArray();
                        Calendar calendar = Calendar.getInstance() ;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-YYYY,hh:mm:ss a");
                        final String dateTime = simpleDateFormat.format(calendar.getTime());
                        UploadTask uploadTask = mStorageRef.child(System.currentTimeMillis() +".png").putBytes(data);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               DatabaseReference userinfo =  storageReference.push();
                               Uri downloadurl = taskSnapshot.getUploadSessionUri();
                                userinfo.child("Search").setValue(text);
                                userinfo.child("Time").setValue( dateTime);
                                userinfo.child("SearchType").setValue("Text");
                                userinfo.child("Image").setValue(downloadurl.toString() );
                                Toast.makeText(QRgenerationActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(QRgenerationActivity.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        a.putExtra("byteArray",bs.toByteArray());
                        a.putExtra("value",text);
                        startActivity(a);



                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                }else
                {
                    Toast.makeText(QRgenerationActivity.this, "TextField is Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
