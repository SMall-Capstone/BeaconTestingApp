package org.androidtown.beacontest2;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.androidtown.android_ibeacon_service.IBeacon;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;



import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;





// 비콘이 쓰이는 클래스는 BeaconConsumer 인터페이스를 구현해야한다.
public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    public static final String TAG = "MainActivity";

    private BeaconManager beaconManager;

    TextView textView_beaconList, textView_myLocation;

    double distance12, distance23, distance31;//비콘끼리의 거리

    private BluetoothManager bluetoothManager; //블루투스 매니저는 기본적으로 있어야하기때문에 여기서는 생략합니다.
    private BluetoothAdapter bluetoothAdapter; //블루투스 어댑터에서 탐색, 연결을 담당하니 여기서는 어댑터가 주된 클래스입니다.

    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();

    private KalmanFilter mKalmanAccRSSI;

    //└============================================================================================================
    public BeaconList beaconList;
    public final int txPower = -59;

    private ArrayList<RssiItem> excelRssiArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //비콘 목록 불러오기(Singleton)
        beaconList = BeaconList.getInstance();

        //SeongWon 수정
        //┌============================================================================================================

        //SeongWon 위치권한 물어보기
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        }
        //위치권한 물어보기 End
        //└============================================================================================================

        //칼만필터 초기화
        //End 칼만필터 초기화
        mKalmanAccRSSI = new KalmanFilter();

        // 실제로 비콘을 탐지하기 위한 비콘매니저 객체를 초기화
        beaconManager = BeaconManager.getInstanceForApplication(this);
        textView_beaconList = (TextView) findViewById(R.id.textView_beaconList);

        // 여기가 중요한데, 기기에 따라서 setBeaconLayout 안의 내용을 바꿔줘야 하는듯 싶다.
        // 필자의 경우에는 아래처럼 하니 잘 동작했음.
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        // 비콘 탐지를 시작한다. 실제로는 서비스를 시작하는것.
        beaconManager.bind(this);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            //블루투스를 지원하지 않거나 켜져있지 않으면 장치를끈다.
            Toast.makeText(this, "블루투스를 켜주세요", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button settingBtn = (Button)findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);*/

                //Test
                ArrayList<BeaconInfo> beaconInfos = beaconList.findNearestBeacons();

                if(beaconInfos==null){
                    Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_SHORT).show();
                }
                else {
                    for(int i=0;i<beaconInfos.size();i++){
                        Log.i("beaconSort",beaconInfos.get(i).getName()+"/"+beaconInfos.get(i).getFilteredRSSIvalue());
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    /*beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                    }*/
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    // 버튼이 클릭되면 textView 에 비콘들의 정보를 뿌린다.
    public void OnButtonClicked(View view) {
        // 아래에 있는 handleMessage를 부르는 함수. 맨 처음에는 0초간격이지만 한번 호출되고 나면
        // 1초마다 불러온다.
        //handler.sendEmptyMessage(0);
        bluetoothAdapter.startLeScan(mLeScanCallback);//탐색시작
    }


    /*윤재 : rssi값 구하기 */
    public BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            if (device.getName() != null && device.getName().contains("MiniBeacon")) {
                BeaconInfo beaconInfo = beaconList.findBeacon(device.getName());
                if(beaconInfo==null){
                    Log.i("BeaconName","beaconInfo를 얻지 못함-"+device.getName());
                    //Toast.makeText(getApplicationContext(),"beacon null / "+device.getName(),Toast.LENGTH_LONG).show();
                }
                else{
                    if (beaconInfo.getIsFirst() == true) {
                        //첫번째 측정되는 RSSI값 버리기
                        beaconInfo.setIsFirst(false);
                    } else {
                        IBeacon iBeacon = new IBeacon();
                        iBeacon = iBeacon.fromScanData(scanRecord, rssi);
                        iBeacon.getTxPower();

                        //최대값 찾는 알고리즘
                        double maxRSSIvalue = beaconInfo.getMaxRSSI();//기존의 max값
                        int filteredRSSI = (int) mKalmanAccRSSI.applyFilter(rssi);//새로 필터링 된 값

                        if (maxRSSIvalue < filteredRSSI) {
                            beaconInfo.setMaxRSSI(filteredRSSI);
                            maxRSSIvalueSetText(beaconInfo.getMinor(), filteredRSSI);//최댓값이 바뀐경우 setText
                        }
                        //최소값 찾는 알고리즘
                        double minRSSIvalue = beaconInfo.getMinRSSI();
                        if (minRSSIvalue > filteredRSSI) {
                            beaconInfo.setMinRSSI(filteredRSSI);
                            minRSSIvalueSetText(beaconInfo.getMinor(), filteredRSSI);//최솟값이 바뀐경우 setText
                        }
                        //새로 필터링 된 값으로 RSSI값 설정
                        beaconInfo.setFilteredRSSIvalue(filteredRSSI);

                        //////excel 용 rssi 데이터 저장
                        excelRssiArray.add(new RssiItem(beaconInfo.getFilteredRSSIvalue(), beaconInfo.getName()));


                        distanceSetText(beaconInfo, (double)filteredRSSI);//거리 계산해서 textView에 출력

                        textView_beaconList.append(beaconInfo.getMinor() + "평균 = " + filteredRSSI + "\n"); //아래쪽 텍스트뷰


                        //Chart 그래프 보는 버튼
                        //chart 로 데이터 전달
                        Button ShowChartButton = (Button) findViewById(R.id.chartButton);
                        ShowChartButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "수정필요", Toast.LENGTH_SHORT).show();
                            /*Intent chartIntent = new Intent(MainActivity.this, ChartActivity.class);
                            //인텐트로 RSSI 값 보내기 인데 안쓰기로 함
                            //chartIntent.putExtra("rssisend" , (int)filteredRSSI14990);
                            startActivity(chartIntent);*/
                            }
                        });

                    }
                }

            }

        }//onLeScan끝

        public void maxRSSIvalueSetText(String minor, int maxRSSI) {
            if (minor.equals("13298")) {
                TextView textView = (TextView) findViewById(R.id.maxRSSIvalue13298);
                textView.setText(maxRSSI + "");
            }
            else if(minor.equals("14990")){
                TextView textView = (TextView) findViewById(R.id.maxRSSIvalue14990);
                textView.setText(maxRSSI + "");
            }
            else if(minor.equals("14997")){
                TextView textView = (TextView) findViewById(R.id.maxRSSIvalue14997);
                textView.setText(maxRSSI + "");
            }
            else if(minor.equals("12802")){
                TextView textView = (TextView) findViewById(R.id.maxRSSIvalue12802);
                textView.setText(maxRSSI + "");
            }
            else if(minor.equals("12928")){
                TextView textView = (TextView) findViewById(R.id.maxRSSIvalue12928);
                textView.setText(maxRSSI + "");
            }else if(minor.equals("14863")){
                TextView textView = (TextView) findViewById(R.id.maxRSSIvalue14863);
                textView.setText(maxRSSI + "");
            }

        }

        public void minRSSIvalueSetText(String minor, int minRSSI) {
            if (minor.equals("13298")) {
                TextView textView = (TextView) findViewById(R.id.minRSSIvalue13298);
                textView.setText(minRSSI + "");
            }
            else if (minor.equals("14990")) {
                TextView textView = (TextView) findViewById(R.id.minRSSIvalue14990);
                textView.setText(minRSSI + "");
            }
            else if (minor.equals("14997")) {
                TextView textView = (TextView) findViewById(R.id.minRSSIvalue14997);
                textView.setText(minRSSI + "");
            }
            else if (minor.equals("12802")) {
                TextView textView = (TextView) findViewById(R.id.minRSSIvalue12802);
                textView.setText(minRSSI + "");
            }
            else if (minor.equals("12928")) {
                TextView textView = (TextView) findViewById(R.id.minRSSIvalue12928);
                textView.setText(minRSSI + "");
            }
            else if (minor.equals("14863")) {
                TextView textView = (TextView) findViewById(R.id.minRSSIvalue14863);
                textView.setText(minRSSI + "");
            }

        }

        public void distanceSetText(BeaconInfo beaconInfo, double filteredRSSI) {
            double d = (double) Math.pow(10, (txPower - filteredRSSI) / (10 * 2));
            double distance = Double.parseDouble(String.format("%.2f",d));
            beaconInfo.setDistance(distance);

            if (beaconInfo.getMinor().equals("13298")) {
                TextView textView = (TextView) findViewById(R.id.textView_distance13298);
                textView.setText(" " + distance);
            }
            else if(beaconInfo.getMinor().equals("14990")){
                TextView textView = (TextView) findViewById(R.id.textView_distance14990);
                textView.setText(" " + distance);
            }
            else if(beaconInfo.getMinor().equals("14997")){
                TextView textView = (TextView) findViewById(R.id.textView_distance14997);
                textView.setText(" " + distance);
            }
            else if(beaconInfo.getMinor().equals("12802")){
                TextView textView = (TextView) findViewById(R.id.textView_distance12802);
                textView.setText(" " + distance);
            }
            else if(beaconInfo.getMinor().equals("12928")){
                TextView textView = (TextView) findViewById(R.id.textView_distance12928);
                textView.setText(" " + distance);
            }
            else if(beaconInfo.getMinor().equals("14863")){
                TextView textView = (TextView) findViewById(R.id.textView_distance14863);
                textView.setText(" " + distance);
            }
        }

    };

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        LinearLayout layout = findViewById(R.id.visibleLayout);

        LinearLayout locationLayout = findViewById(R.id.visibleLayout);

        int result = layout.getVisibility();
        int locationresult = layout.getVisibility();

        if (id == R.id.saveExcel) {
            saveExcel();
            return true;
        }

        else if(id == R.id.visibleBtn) {
            if (result == View.VISIBLE) {
                layout.setVisibility(View.GONE); // 화면에서 제외
            } else {
                layout.setVisibility(View.VISIBLE); // 화면에서 보이기
            }
        }

        else if(id == R.id.locationBtn) {
            if(locationresult == View.VISIBLE) {
                locationLayout.setVisibility(View.GONE);
            } else {
                locationLayout.setVisibility(View.VISIBLE);
            }

        }

        return super.onOptionsItemSelected(item);
    }




    public void saveExcel(){

        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet(); // 새로운 시트 생성

        Row row = sheet.createRow(0); // 새로운 행 생성
        Cell cell;

        cell = row.createCell(0); // 1번 셀 생성
        cell.setCellValue("이름"); // 1번 셀 값 입력

        cell = row.createCell(1); // 2번 셀 생성
        cell.setCellValue("RSSI"); // 2번 셀 값 입력

        for(int i=1;i<excelRssiArray.size();i++) {
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(excelRssiArray.get(i).getBeaconID());
            cell = row.createCell(1);
            cell.setCellValue(excelRssiArray.get(i).getRssi());
        }

        File xlsFile = new File("sdcard/excelRssi/RssiData.xls");
        // File xlsFile = new File(getExternalFilesDir(null),"test2.xls");
        try{
            FileOutputStream os = new FileOutputStream(xlsFile);
            workbook.write(os); // 외부 저장소에 엑셀 파일 생성
            Log.i("yunjae_excel", "엑셀 파일생성");
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}