package com.androidhive.xmlparsing;

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
        
        // Get XML values from previous intent
        String home = in.getStringExtra(AndroidXMLParsingActivity.HOME_TEAM);
        String visitor = in.getStringExtra(AndroidXMLParsingActivity.VISITOR_TEAM);
        String home_score = in.getStringExtra(AndroidXMLParsingActivity.HOME_SCORE);
        String visitor_score = in.getStringExtra(AndroidXMLParsingActivity.VISITOR_SCORE);
        
        // Displaying all values on the screen
        TextView lblHome = (TextView) findViewById(R.id.home_label);
        TextView lblVisitor = (TextView) findViewById(R.id.visitor_label);
        TextView lblHScore = (TextView) findViewById(R.id.home_score_label);
        TextView lblVScore = (TextView) findViewById(R.id.visitor_score_label);

        lblHome.setText(home);
        lblVisitor.setText(visitor);
        lblHScore.setText(home_score);
        lblVScore.setText(visitor_score);
    }
}
