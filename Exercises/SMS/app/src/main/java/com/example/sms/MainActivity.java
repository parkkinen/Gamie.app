package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnSendSMS = null;
    private EditText txtPhoneNo = null;
    private EditText txtMessage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendSMS = findViewById(R.id.btnSendSMS);
        txtPhoneNo = findViewById(R.id.txtPhoneNo);
        txtMessage = findViewById(R.id.txtMessage);

        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = txtPhoneNo.getText().toString();
                String message = txtMessage.getText().toString();
                if (phoneNo.length() > 0 && message.length() > 0) {
                    sendSMS(phoneNo, message);
                } else {
                    Toast.makeText(getBaseContext(), "Please enter both number and message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager smgr = SmsManager.getDefault();
        smgr.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
