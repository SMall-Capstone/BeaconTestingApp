package org.androidtown.beacontest2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    BeaconList beaconList;
    Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //비콘 목록 불러오기(Singleton)
        beaconList = BeaconList.getBeaconListInstance();
        map = Map.getMapInstance();

        Button updateBtn = (Button)findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLocation();
            }
        });
    }

    public void updateLocation(){
        double x,y;
        double maxWidth,maxHeight;
        int txPower;

        x = Double.parseDouble(((EditText)findViewById(R.id.editText_12802_x)).getText().toString());
        y = Double.parseDouble(((EditText)findViewById(R.id.editText_12802_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon12802").setLocation(x,y);

        x = Double.parseDouble(((EditText)findViewById(R.id.editText_12928_x)).getText().toString());
        y = Double.parseDouble(((EditText)findViewById(R.id.editText_12928_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon12928").setLocation(x,y);

        x = Double.parseDouble(((EditText)findViewById(R.id.editText_13298_x)).getText().toString());
        y = Double.parseDouble(((EditText)findViewById(R.id.editText_13298_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon13298").setLocation(x,y);

        x = Double.parseDouble(((EditText)findViewById(R.id.editText_14863_x)).getText().toString());
        y = Double.parseDouble(((EditText)findViewById(R.id.editText_14863_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon_14863").setLocation(x,y);

        x = Double.parseDouble(((EditText)findViewById(R.id.editText_14990_x)).getText().toString());
        y = Double.parseDouble(((EditText)findViewById(R.id.editText_14990_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon_14990").setLocation(x,y);

        x = Double.parseDouble(((EditText)findViewById(R.id.editText_14997_x)).getText().toString());
        y = Double.parseDouble(((EditText)findViewById(R.id.editText_14997_y)).getText().toString());
        beaconList.findBeacon("MiniBeacon_14997").setLocation(x,y);

        maxWidth = Double.parseDouble(((EditText)findViewById(R.id.editText_maxWidth)).getText().toString());
        maxHeight = Double.parseDouble(((EditText)findViewById(R.id.editText_maxHeight)).getText().toString());
        map.setSize(maxWidth,maxHeight);

        txPower = Integer.parseInt(((EditText)findViewById(R.id.editText_txPower)).getText().toString());
        beaconList.setTxPower(txPower);

        Toast.makeText(getApplicationContext(),"설정완료",Toast.LENGTH_SHORT).show();
        finish();

    }
}
