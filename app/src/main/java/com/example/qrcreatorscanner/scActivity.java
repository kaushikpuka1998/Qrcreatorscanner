package com.example.qrcreatorscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class scActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    DatabaseReference ref;
    private static final int REQUEST_CAMERA =1;
    private ZXingScannerView scannerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sc);
        scannerview = new ZXingScannerView(this);
        setContentView(scannerview);
        ref = FirebaseDatabase.getInstance().getReference("QR_ScanResults");

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
                if(checkpermission())
                {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                }else
                {
                    requestPermission();
                }
        }
    }
    private boolean checkpermission()
    {
            return (ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED);

    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{CAMERA}, REQUEST_CAMERA);
    }
    public void onRequestPermissionsResult(int requestcode,String permission[],int grantResults[])
    {
        switch(requestcode)
        {
            case REQUEST_CAMERA:
                if(grantResults.length > 0)
                {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted)
                    {
                        Toast.makeText(this, "Permisson Granted", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(this, "Permisson Denied", Toast.LENGTH_SHORT).show();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            if(shouldShowRequestPermissionRationale(CAMERA))
                            {
                                displayAlertMessage("You Need to allow access for the permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
                                        {
                                            requestPermissions(new String[]{CAMERA},REQUEST_CAMERA);
                                        }

                                    }
                                });
                                return;
                            }
                        }
                    }
                    break;
                }
        }
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(scActivity.this).setMessage(message).setPositiveButton("OK",listener).setNegativeButton("Cancel",null).create().show();
    }
    @Override
    public void handleResult(final Result result) {
        final String scanResult = result.getText().toString().trim();
        Calendar calendar = Calendar.getInstance() ;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-YYYY,hh:mm:ss a");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        String id = ref.push().getKey() ;
        model mode = new model(scanResult,dateTime);
        ref.child(id).setValue(mode);
        Toast.makeText(this, "Result Saved to Database", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerview.resumeCameraPreview(scActivity.this);
            }
        });
        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerview.stopCamera();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        scannerview.startCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkpermission())
            {
                if(scannerview == null)
                {
                    scannerview = new ZXingScannerView(this);
                    setContentView(scannerview);
                }
                scannerview.setResultHandler(this);
                scannerview.startCamera();
            }
            else
            {
                requestPermission();
            }
        }
        scannerview.startCamera();
    }
}
