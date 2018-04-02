package org.androidtown.beacontest2;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AnimatedView extends SurfaceView implements SurfaceHolder.Callback{
    Context context;
    //ArrayList<Ball> balls= new ArrayList<Ball>();
    Ball ball = Ball.getBallInstance();
    TimerThread timerThread;
    SurfaceHolder surfaceHolder;
    Bitmap bitmap;
    Paint mPaint;

    public AnimatedView(Context context) {
        super(context);
        this.context= context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        //surfaceHolder.setFixedSize(300, 500);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mapimage140);
        mPaint = new Paint();
    }

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*timerThread= new TimerThread();
        timerThread.start();*/ //onSizeChanged->surfaceCreated
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        timerThread= new TimerThread();
        timerThread.start();
        Log.i("check Thread","스레드시작");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    public void stop(){
        if(timerThread != null){

            timerThread.isRun= false; //timerThread.interrupt();
            try {
                timerThread.join();
            }catch(InterruptedException e){
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("yunjae", "onDraw");
        //updateAnimation();
        ball.draw(canvas);
        invalidate();
    }


    Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };

    class TimerThread extends Thread {
        public boolean  isRun= true;
        int ticker = 0;
        public void run(){
            while(isRun){
                Log.i("jmlee", "thread is running");

                //handler.sendEmptyMessage(0);
                // part1 : paint
                if(ticker++ % 10 == 0) {
                    synchronized (surfaceHolder) {
                        Canvas canvas = surfaceHolder.lockCanvas();

                        if(canvas==null){
                            Log.i("canvas", "canvas null");
                        }
                        else{
                            canvas.drawColor(Color.WHITE);

                            BeaconList beaconList = BeaconList.getBeaconListInstance();
                            beaconList.calculateDistance();

                            canvas.drawBitmap(bitmap, 50, 0, null);
                            Log.i("canvas", "x = " + canvas.getWidth() + " y = " + canvas.getHeight());
                            try{
                                ball.draw(canvas);
                                beaconList.initNearestPoint();
                                sleep(1500);
                            }catch(InterruptedException e){
                                break;
                            }


                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }

                    }
                }
                // part2 : update
                //updateAnimation();

            }
        }
    }
}

