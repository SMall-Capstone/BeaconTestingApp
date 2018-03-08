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
        //String s = ((EditText)findViewById(R.id.editText_13298_x)).getText().toString();


    }
}
