package org.androidtown.beacontest2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    BeaconList beaconList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //비콘 목록 불러오기(Singleton)
        beaconList = BeaconList.getInstance();

        Button updateBtn = (Button)findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLocation();
            }
        });
    }

    public void updateLocation(){
        int x,y;

        x = Integer.parseInt(((EditText)findViewById(R.id.editText_12802_x)).getText().toString());
        y = Integer.parseInt(((EditText)findViewById(R.id.editText_12802_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon12802").setLocation(x,y);

        x = Integer.parseInt(((EditText)findViewById(R.id.editText_12928_x)).getText().toString());
        y = Integer.parseInt(((EditText)findViewById(R.id.editText_12928_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon12928").setLocation(x,y);

        x = Integer.parseInt(((EditText)findViewById(R.id.editText_13298_x)).getText().toString());
        y = Integer.parseInt(((EditText)findViewById(R.id.editText_13298_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon13298").setLocation(x,y);

        x = Integer.parseInt(((EditText)findViewById(R.id.editText_14863_x)).getText().toString());
        y = Integer.parseInt(((EditText)findViewById(R.id.editText_14863_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon_14863").setLocation(x,y);

        x = Integer.parseInt(((EditText)findViewById(R.id.editText_14990_x)).getText().toString());
        y = Integer.parseInt(((EditText)findViewById(R.id.editText_14990_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon_14990").setLocation(x,y);

        x = Integer.parseInt(((EditText)findViewById(R.id.editText_14997_x)).getText().toString());
        y = Integer.parseInt(((EditText)findViewById(R.id.editText_14997_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon_14997").setLocation(x,y);
    }
}
