package org.androidtown.beacontest2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new AnimatedView(this));
    }
}
