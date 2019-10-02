package com.androidhive.jsonparsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleMenuItemActivity  extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String name = in.getStringExtra(AndroidJSONParsingActivity.TAG_NAME);
        String address = in.getStringExtra(AndroidJSONParsingActivity.TAG_ADDRESS);
        String ip = in.getStringExtra(AndroidJSONParsingActivity.TAG_IP);
        String port = in.getStringExtra(AndroidJSONParsingActivity.TAG_PORT);

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblAddr = (TextView) findViewById(R.id.address_label);
        TextView lblIp = (TextView) findViewById(R.id.ip_label);
        
        lblName.setText(name);
        lblAddr.setText(address);
        lblIp.setText("IP: " + ip);
    }
}
