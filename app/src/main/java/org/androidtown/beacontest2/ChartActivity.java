package org.androidtown.beacontest2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.androidtown.android_ibeacon_service.IBeacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements BeaconConsumer {

    LineChart chart;
    int X_RANGE = 50;
    int DATA_RANGE = 100/*30*/;

    ArrayList<Entry> xVal;
    LineDataSet setXcomp;
    ArrayList<String> xVals;

    ArrayList<ILineDataSet> lineDataSets;
    LineData lineData;
    //Sub Activity 에 비콘 정보 받아오기 도전
    //┌============================================================================================================

    private KalmanFilter mKalmanAccRSSI2;
    private BluetoothManager bluetoothManager2; //블루투스 매니저는 기본적으로 있어야하기때문에 여기서는 생략합니다.
    private BluetoothAdapter bluetoothAdapter2; //블루투스 어댑터에서 탐색, 연결을 담당하니 여기서는 어댑터가 주된 클래스입니다.
    public ArrayList<Integer> rssiArray = new ArrayList<Integer>();

    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    private HashMap<String, Integer> beaconRSSI = new HashMap<String, Integer>();

    private BeaconManager beaconManager2;
    // 감지된 비콘들을 임시로 담을 리스트
    private List<Beacon> beaconList2 = new ArrayList<>();
    private double filteredRSSI14990;



    //└============================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rssi_chart);



        //Sub Activity 에 비콘 정보 받아오기 도전
        //┌============================================================================================================

        mKalmanAccRSSI2 = new KalmanFilter();
        beaconManager2 = BeaconManager.getInstanceForApplication(this);
        beaconManager2.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager2.bind(this);

        bluetoothManager2 = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter2 = bluetoothManager2.getAdapter();

        if (bluetoothAdapter2 == null || !bluetoothAdapter2.isEnabled()) {
            //블루투스를 지원하지 않거나 켜져있지 않으면 장치를끈다.
            Toast.makeText(this, "블루투스를 켜주세요", Toast.LENGTH_SHORT).show();
            finish();
        }
        bluetoothAdapter2.startLeScan(mLeScanCallback);


        //└============================================================================================================
        init();
        threadStart();
    }

    private void init() {
        chart = (LineChart) findViewById(R.id.chart);
        chartInit();
    }

    private void chartInit() {
        //chart.setAutoScaleMinMaxEnabled(true);


        //Y축 좌표 자동으로 안움직이게 고정시킴
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setLabelCount(10, false);
        yAxis.setAxisMaxValue(-20);
        yAxis.setAxisMinValue(-120);
        //End Y축 좌표 자동으로 안움직이게 고정시킴

        xVal = new ArrayList<Entry>();

        setXcomp = new LineDataSet(xVal, "filteredRSSI14990");

        setXcomp.setColor(Color.RED);
        setXcomp.setDrawValues(false);
        setXcomp.setDrawCircles(false);
        setXcomp.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSets = new ArrayList<ILineDataSet>();
        lineDataSets.add(setXcomp);

        xVals = new ArrayList<String>();
        for (int i = 0; i < X_RANGE; i++) {
            xVals.add("");
        }
        lineData = new LineData(xVals, lineDataSets);
        chart.setData(lineData);
        chart.invalidate();
    }

    public void chartUpdate(int x) {
        if (xVal.size() > DATA_RANGE) {
            xVal.remove(0);
            for (int i = 0; i < DATA_RANGE; i++) {
                xVal.get(i).setXIndex(i);
            }
        }
        xVal.add(new Entry(x, xVal.size()));
        setXcomp.notifyDataSetChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    //┌ 그래프에 들어오는 Y 축값 조절

    Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) { // Message id 가 0 이면
                int a = 0;
                //a = (int) (Math.random() * 100);
                /*Intent newIntent = getIntent();
                a = newIntent.getIntExtra("rssisend", 1024);*/
                a = (int)filteredRSSI14990;

                chartUpdate(a);

            }
        }
    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    //└ End 그래프에 들어오는 Y 축값 조절

    public class MyThread extends Thread {

        public void run() {
            while (true) {
                handler.sendEmptyMessage(0);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void threadStart() {
        MyThread thread = new MyThread();
        thread.setDaemon(true);
        thread.start();
    }

    //Sub Activity 에 비콘 정보 받아오기 도전
    //┌============================================================================================================
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager2.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager2.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    beaconList2.clear();
                    for (Beacon beacon : beacons) {
                        beaconList2.add(beacon);
                    }
                }
            }
        });

        try {
            beaconManager2.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }
    public BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (device.getName() != null) {
                if ((device.getName()).contains("14990")) {
                    IBeacon iBeacon = new IBeacon();
                    iBeacon = iBeacon.fromScanData(scanRecord, rssi);
                    iBeacon.getTxPower();

                    filteredRSSI14990 =  (int)mKalmanAccRSSI2.applyFilter(rssi);

                }
            }

        }
    };

//└============================================================================================================
}

