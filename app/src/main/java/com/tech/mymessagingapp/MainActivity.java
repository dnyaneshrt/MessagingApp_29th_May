package com.tech.mymessagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    CountryCodePicker cp_code;
    EditText et_mobile_number, et_msg;
    Button btn_call, btn_sms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cp_code = findViewById(R.id.cp_code);
        et_mobile_number = findViewById(R.id.et_number);
        et_msg = findViewById(R.id.et_msg);
        btn_call = findViewById(R.id.btn_call);
        btn_sms = findViewById(R.id.btn_sms);


btn_sms.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     int status=ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS);

     if(status==PackageManager.PERMISSION_GRANTED)
     {
         msgDemo();
     }else
     {
         ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},50);
     }
    }
});

  //this code will be exceuted after clicking on btn call
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //step -1   check the permission status.
                int status = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
                //stpe 2
                if (status == PackageManager.PERMISSION_GRANTED) {
                    //here i will write logic for calling
                    callingDemo();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 10);
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&requestCode==10) {
            callingDemo();
        } else if(grantResults[0]==PackageManager.PERMISSION_GRANTED&&requestCode==50) {
            msgDemo();
        }
        else {
//            Toast.makeText(this, "user is not alloed to call", Toast.LENGTH_SHORT).show();
            Toasty.warning(this, "user is not allowed here", Toast.LENGTH_SHORT, true).show();
        }
    }

    //
    private void callingDemo() {

//   String country_code = cp_code.getSelectedCountryCode();
        String mobile_number = et_mobile_number.getText().toString();
//        String mobile_number = country_code + number;//concatenation

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mobile_number));
        startActivity(intent);
    }

    private void msgDemo() {

        String mobile_number = et_mobile_number.getText().toString();
        String message=et_msg.getText().toString();
        String[] numbers=mobile_number.split(",");

        for(String number:numbers)
        {
            SmsManager smsManager= SmsManager.getDefault();

            //pending intent which calls after some time
            Intent si=new Intent(this,SentActivity.class);
            PendingIntent send_intent=PendingIntent.getActivity(this,15,si,0);

            Intent di=new Intent(this,DelieveredActivity.class);
            PendingIntent deliverd_intent=PendingIntent.getActivity(this,15,di,0);

            smsManager.sendTextMessage(number,null,message,send_intent,deliverd_intent);

        }

    }

}