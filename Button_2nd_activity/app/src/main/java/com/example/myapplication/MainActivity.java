package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    static ArrayList<String> carList = new ArrayList<>();
    private EditText et = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carList.add("Alfa Romeo");
        carList.add("BMW");
        carList.add("Corvette");

        final ListView lw = (ListView)findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carList);
        lw.setAdapter(adapter);
        et = (EditText)findViewById(R.id.editText);
        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carList.add(et.getText().toString());
                lw.setAdapter(adapter);
            }
        });
        findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        carList.set(i, et.getText().toString());
                        lw.setAdapter(adapter);
                        lw.setOnItemClickListener(null);
                    }
                });
            }
        });
        findViewById(R.id.removeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        carList.remove(i);
                        lw.setAdapter(adapter);
                        lw.setOnItemClickListener(null);
                    }
                });
            }
        });
        findViewById(R.id.secondActButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
