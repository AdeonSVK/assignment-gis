package sk.fiit.xrackol.evcharging;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

import sk.fiit.xrackol.evcharging.java.Settings;

public class SearchActivity extends AppCompatActivity {

    Button btn_nearby;
    Button btn_along;
    Button btn_somewhere;

    EditText et_nearby;
    EditText et_along;
    EditText et_along_road;
    EditText et_somewhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btn_nearby = (Button) findViewById(R.id.btn_srch_search_nearby);
        btn_along = (Button) findViewById(R.id.btn_srch_search_along);
        btn_somewhere = (Button) findViewById(R.id.btn_srch_search_somewhere);

        et_nearby = (EditText)findViewById(R.id.et_srch_nearby);
        et_along = (EditText)findViewById(R.id.et_srch_along);
        et_along_road = (EditText)findViewById(R.id.et_srch_along_road);
        et_somewhere = (EditText)findViewById(R.id.et_srch_somewhere);

        btn_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.settings.setSearchType("nearby");
                Settings.settings.setDistance(Integer.parseInt(et_nearby.getText().toString())*1000);
                Settings.searchApplied = true;
                finish();
            }
        });

        btn_along.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.settings.setSearchType("along");
                Settings.settings.setDistance(!et_along.getText().toString().equals("") ? Integer.parseInt(et_along.getText().toString())*1000 : 10000);
                Settings.settings.setObjectName(et_along_road.getText().toString());
                Settings.searchApplied = true;
                finish();
            }
        });

        btn_somewhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.settings.setSearchType("somewhere");
                Settings.settings.setObjectName(et_somewhere.getText().toString());
                Settings.searchApplied = true;
                finish();
            }
        });


    }
}
