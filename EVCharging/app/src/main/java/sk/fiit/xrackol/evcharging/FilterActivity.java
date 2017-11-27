package sk.fiit.xrackol.evcharging;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import sk.fiit.xrackol.evcharging.java.Settings;

public class FilterActivity extends AppCompatActivity {

    Button btn_submit;
    CheckBox mennekes;
    CheckBox chademo;
    CheckBox ccs;
    CheckBox cee5;
    CheckBox cee75;
    CheckBox mennekest;
    CheckBox iec;
    CheckBox tesla;
    CheckBox j1772;

    EditText kwFrom;
    EditText kwTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        mennekes = (CheckBox) findViewById(R.id.cb_flt_mennekes);
        chademo = (CheckBox) findViewById(R.id.cb_flt_chademo);
        ccs = (CheckBox) findViewById(R.id.cb_flt_ccs);
        cee5 = (CheckBox) findViewById(R.id.cb_flt_cee5);
        cee75 = (CheckBox) findViewById(R.id.cb_flt_cee75);
        mennekest = (CheckBox) findViewById(R.id.cb_flt_mennekes_tethered);
        iec = (CheckBox) findViewById(R.id.cb_flt_iec);
        tesla = (CheckBox) findViewById(R.id.cb_flt_tesla);
        j1772 = (CheckBox) findViewById(R.id.cb_flt_j1772);

        kwFrom = (EditText) findViewById(R.id.et_flt_kwfrom);
        kwTo = (EditText) findViewById(R.id.et_flt_kwto);

        if(Settings.settings.isMennekes()) mennekes.setChecked(true);
        if(Settings.settings.isChademo()) chademo.setChecked(true);
        if(Settings.settings.isCcs()) ccs.setChecked(true);
        if(Settings.settings.isCee5()) cee5.setChecked(true);
        if(Settings.settings.isCee75()) cee75.setChecked(true);
        if(Settings.settings.ismennekest()) mennekest.setChecked(true);
        if(Settings.settings.isIec()) iec.setChecked(true);
        if(Settings.settings.isTesla()) tesla.setChecked(true);
        if(Settings.settings.isJ1772()) j1772.setChecked(true);
        if(Settings.settings.getKwFrom() > 0) kwFrom.setText((int)Settings.settings.getKwFrom()+"");
        if(Settings.settings.getKwTo() > 0) kwTo.setText((int)Settings.settings.getKwTo()+"");

        btn_submit = (Button) findViewById(R.id.btn_flt_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Settings.settings.setMennekes(mennekes.isChecked());
                Settings.settings.setChademo(chademo.isChecked());
                Settings.settings.setCcs(ccs.isChecked());
                Settings.settings.setCee5(cee5.isChecked());
                Settings.settings.setCee75(cee75.isChecked());
                Settings.settings.setmennekest(mennekest.isChecked());
                Settings.settings.setIec(iec.isChecked());
                Settings.settings.setTesla(tesla.isChecked());
                Settings.settings.setJ1772(j1772.isChecked());
                Settings.settings.setKwFrom(kwFrom.getText().toString().length() > 0 ? Double.parseDouble(kwFrom.getText().toString()):0);
                Settings.settings.setKwTo(kwTo.getText().toString().length() > 0 ? Double.parseDouble(kwTo.getText().toString()):0);
                Settings.filterChanged = true;
                finish();

            }
        });
    }
}
