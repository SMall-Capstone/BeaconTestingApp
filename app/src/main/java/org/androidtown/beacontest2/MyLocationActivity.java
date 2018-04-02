package org.androidtown.beacontest2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MyLocationActivity extends AppCompatActivity implements View.OnClickListener{

    double previousX=0,previousY=0;
    double resultX,resultY;
    Animation ani;
    ImageView img;

    BeaconList beaconList = BeaconList.getBeaconListInstance();

    class TimeThread extends Thread{
        public void run(){
            while(true){
                beaconList.calculateDistance();

                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

//                            previousX = beaconList.getPreviousX();
//                            previousY = beaconList.getPreviousY();
                            resultX = beaconList.getResultX();
                            resultY = beaconList.getResultY();
                            Log.i("Ball_Animation_update", "\npreviousX = " + (float) previousX + "\n" + "previousY = " + (float) previousY);
                            ani = new TranslateAnimation( (float) (previousX*MainActivity.accumulationX), (float) (resultX*MainActivity.accumulationX),
                                    (float)(previousY*MainActivity.accumulationY), (float)(resultY*MainActivity.accumulationY) );
//                                            ani = new TranslateAnimation( 1, 111, 1, 111 );

                            ani.setDuration(1500);   //애니매이션 지속 시간
                            ani.setFillAfter(true);  // animation를 setFillAfter를 이용하여 animation후에 그대로 있도록 합니다.
                            img.setVisibility(View.VISIBLE);
                            //img.setAnimation(ani);
                            img.startAnimation(ani);
                            //ani.start();



//                            Log.i("Ball_Animation_update", "Values = " + (float) previousX + "\n" +  (float) (resultX*MainActivity.accumulationX) + "\n" +
//                                    (float)previousY + "\n" + (float)(resultY*MainActivity.accumulationY));

                            previousX = resultX;
                            previousY = resultY;


                        }
                    });

                    beaconList.initNearestPoint();
                    sleep(3000);

                }catch(Exception e){

                }
            }



        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(/*new AnimatedView(this)*/ R.layout.activity_my_location);

        img = (ImageView)findViewById(R.id.RedPoint);
        TimeThread thread = new TimeThread();
        thread.start();

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}