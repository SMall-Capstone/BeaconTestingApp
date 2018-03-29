package org.androidtown.beacontest2;

import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by 이예지 on 2018-03-07.
 */

public class BeaconList {
    HashMap<String,BeaconInfo> beaconInfoHashMap = new HashMap<String,BeaconInfo>();
    private static BeaconList beaconList = new BeaconList();//싱글톤 패턴 적용
    ArrayList<String> beaconId = new ArrayList<String>();
    ArrayList<BeaconInfo> beaconInfos = new ArrayList<BeaconInfo>();
    ArrayList<BeaconInfo> beaconInfosSortByPoint = new ArrayList<BeaconInfo>();
    private int txPower;
    public Ball ball = Ball.getBallInstance();
    private double previousX=-1,previousY=-1;

    private BeaconList() {
        beaconInfoHashMap.put("MiniBeacon_00165",new BeaconInfo("MiniBeacon_00165","165"));
        beaconInfoHashMap.get("MiniBeacon_00165").setLocation(3,3);

        beaconInfoHashMap.put("MiniBeacon_00175",new BeaconInfo("MiniBeacon_00175","175"));
        beaconInfoHashMap.get("MiniBeacon_00175").setLocation(8,3);

        beaconInfoHashMap.put("MiniBeacon_00177",new BeaconInfo("MiniBeacon_00177","177"));
        beaconInfoHashMap.get("MiniBeacon_00177").setLocation(0,8);

        beaconInfoHashMap.put("MiniBeacon_00783",new BeaconInfo("MiniBeacon_00783","783"));
        beaconInfoHashMap.get("MiniBeacon_00783").setLocation(5,8);

        beaconInfoHashMap.put("MiniBeacon_01031",new BeaconInfo("MiniBeacon_01031","1031"));
        beaconInfoHashMap.get("MiniBeacon_01031").setLocation(10,8);

        beaconInfoHashMap.put("MiniBeacon_01352",new BeaconInfo("MiniBeacon_01352","1352"));
        beaconInfoHashMap.get("MiniBeacon_01352").setLocation(7,13);

        beaconInfoHashMap.put("MiniBeacon12802",new BeaconInfo("MiniBeacon12802","12802"));
        beaconInfoHashMap.get("MiniBeacon12802").setLocation(12,13);

        beaconInfoHashMap.put("MiniBeacon12928",new BeaconInfo("MiniBeacon12928","12928"));
        beaconInfoHashMap.get("MiniBeacon12928").setLocation(9,18);

        beaconInfoHashMap.put("MiniBeacon13298",new BeaconInfo("MiniBeacon13298","13298"));
        beaconInfoHashMap.get("MiniBeacon13298").setLocation(11,23);

        beaconInfoHashMap.put("MiniBeacon_14863",new BeaconInfo("MiniBeacon_14863","14863"));
        beaconInfoHashMap.get("MiniBeacon_14863").setLocation(13,3);

        beaconInfoHashMap.put("MiniBeacon_14990",new BeaconInfo("MiniBeacon_14990","14990"));
        beaconInfoHashMap.get("MiniBeacon_14990").setLocation(2,13);

        beaconInfoHashMap.put("MiniBeacon_14997",new BeaconInfo("MiniBeacon_14997","14997"));
        beaconInfoHashMap.get("MiniBeacon_14997").setLocation(14,18);

        beaconId.add("MiniBeacon_00165");
        beaconId.add("MiniBeacon_00175");
        beaconId.add("MiniBeacon_00177");
        beaconId.add("MiniBeacon_00783");
        beaconId.add("MiniBeacon_01031");
        beaconId.add("MiniBeacon_01352");
        beaconId.add("MiniBeacon12802");
        beaconId.add("MiniBeacon12928");
        beaconId.add("MiniBeacon13298");
        beaconId.add("MiniBeacon_14863");
        beaconId.add("MiniBeacon_14990");
        beaconId.add("MiniBeacon_14997");

    }

    public BeaconInfo findBeacon(String name){
        return beaconInfoHashMap.get(name);
    }

    public static BeaconList getBeaconListInstance(){
        return beaconList;
    }


    public ArrayList<BeaconInfo> findNearestBeaconsByRssi(){

        beaconInfos.clear();

        for(int i=0;i<beaconInfoHashMap.size();i++){
            beaconInfos.add(beaconInfoHashMap.get(beaconId.get(i)));
        }

        if(beaconInfos.size()==beaconInfoHashMap.size()){
            //Collections.sort(beaconInfos);
            Collections.sort(beaconInfos, new Comparator<BeaconInfo>() {
                @Override
                public int compare(BeaconInfo beaconInfo1, BeaconInfo beaconInfo2) {
                    if(beaconInfo1.getFilteredRSSIvalue() < beaconInfo2.getFilteredRSSIvalue()){
                        return 1;
                    }
                    else if(beaconInfo1.getFilteredRSSIvalue() > beaconInfo2.getFilteredRSSIvalue()){
                        return -1;
                    }
                    else
                        return 0;
                }
            });

        }
        else {
            Log.i("Sort","beaconInfos setting fail");
        }

        beaconInfos.get(0).addNearestPoint(3);
        beaconInfos.get(1).addNearestPoint(2);
        beaconInfos.get(2).addNearestPoint(1);

        return beaconInfos;
    }

    public ArrayList<BeaconInfo> findNearestBeaconsByPoint(){
        beaconInfosSortByPoint.clear();
        beaconInfosSortByPoint.addAll(beaconInfos);

        if(beaconInfosSortByPoint.size()==beaconInfoHashMap.size()){
            //Collections.sort(beaconInfos);

            Collections.sort(beaconInfosSortByPoint, new Comparator<BeaconInfo>() {
                @Override
                public int compare(BeaconInfo beaconInfo1, BeaconInfo beaconInfo2) {
                    if(beaconInfo1.getNearestPoint() < beaconInfo2.getNearestPoint()){
                        return 1;
                    }
                    else if(beaconInfo1.getNearestPoint() > beaconInfo2.getNearestPoint()){
                        return -1;
                    }
                    else
                        return 0;
                }
            });

        }
        else {
            Log.i("Sort","beaconInfos setting fail");
        }

        return beaconInfosSortByPoint;
    }

    public void initNearestPoint() {
        for (int i = 0; i < beaconInfosSortByPoint.size(); i++) {
            beaconInfosSortByPoint.get(i).setNearestPoint(0);
        }
    }

    public void calculateDistance(){
        double resultX,resultY;
        BeaconInfo b1, b2,  b3;
        ArrayList<BeaconInfo> beaconInfosSortByPoint = findNearestBeaconsByPoint();
        for (int i=0;i<beaconInfosSortByPoint.size();i++){
            Log.i("NearestPoint", beaconInfosSortByPoint.get(i).getName() + " => " + beaconInfosSortByPoint.get(i).getNearestPoint());
        }
        b3 = beaconInfosSortByPoint.get(0);

        b1 = beaconInfosSortByPoint.get(1);
        b2 = beaconInfosSortByPoint.get(2);

        /*Log.i("NearestPoint", "============================================================================");
        Log.i("NearestPoint", "calculateDistance");
        Log.i("NearestPoint", b1.getName() + " => " + b1.getNearestPoint());
        Log.i("NearestPoint", b2.getName() + " => " + b2.getNearestPoint());
        Log.i("NearestPoint", b3.getName() + " => " + b3.getNearestPoint());*/


        double X1 = b1.getLocation_x();
        double Y1 = b1.getLocation_y();
        double X2 = b2.getLocation_x();
        double Y2 = b2.getLocation_y();
        double D1 = b1.getDistance();
        double D2 = b2.getDistance();

        double T = Math.log( Math.pow((X2 - X1),2) + Math.pow((Y2 - Y1),2));
        double TrianglePlusX = X1 + D1 * Math.cos( Math.atan( (Y2 - Y1) / (X2 - X1) ) +
                Math.acos( (Math.pow(D1,2) - Math.pow(D2,2) + Math.pow(T,2) ) / (2 * D1 * T) ) );
        double TriangleMinusX = X1 + D1 * Math.cos( Math.atan( (Y2 - Y1) / (X2 - X1) ) -
                Math.acos( (Math.pow(D1,2) - Math.pow(D2,2) + Math.pow(T,2) ) / (2 * D1 * T) ) );
        double TrianglePlusY = Y1 + D1 * Math.sin( Math.atan( (Y2 - Y1) / (X2 - X1) ) +
                Math.acos( (Math.pow(D1,2) -  Math.pow(D2, 2) + Math.pow(T,2) ) / (2 * D1 * T) ) );
        double TriangleMinusY = Y1 + D1 * Math.sin( Math.atan( (Y2 - Y1) / (X2 - X1) ) -
                Math.acos( (Math.pow(D1,2) -  Math.pow(D2, 2) + Math.pow(T,2) ) / (2 * D1 * T) ) );


        double d1 = pointTopointDistance(TrianglePlusX,TrianglePlusY,b3.getLocation_x(),b3.getLocation_y());
        double d2 = pointTopointDistance(TriangleMinusX,TriangleMinusY,b3.getLocation_x(),b3.getLocation_y());
        if(d1 < d2){
            resultX = TrianglePlusX;
            resultY = TrianglePlusY;
        }
        else {
            resultX = TriangleMinusX;
            resultY = TriangleMinusY;
        }

        Map m = Map.getMapInstance();
        if(resultX < 0 || resultX > m.getMaxWidth()) {
            if(resultX<0)
                resultX = 0;
            if(resultX>m.getMaxWidth())
                resultX = m.getMaxWidth();
        }
        if(resultY < 0 || resultX > m.getMaxHeight()) {
            if(resultY<0)
                resultY = 0;
            if(resultY>m.getMaxHeight())
                resultY = m.getMaxHeight();
        }

        //좌표가 NaN으로 나올 경우 이전값으로 대체하여 사용
        if(Double.isNaN(resultX) || Double.isNaN(resultY)){
            resultX = previousX;
            resultY = previousY;
        }

        if(previousX==-1 && previousY==-1) {
            //이전에 저장된 값이 없는 경우=>처음 측정된 값
            previousX=resultX;
            previousY=resultY;

            ball.setLocation(resultX*MainActivity.accumulationX, resultY*MainActivity.accumulationY);
            Log.i("yunjae", "x = " + resultX + " y = " + resultY);
        }
        else {
            //이전의 값과 차이가 설정 값 이상 나지 않는 경우에만 좌표출력
            if ( ! (previousX-resultX<-10 || previousX-resultX>10) ){
                previousX=resultX;
                previousY=resultY;
                //ball.setLocation((float)resultX*(float)62.29, (float)resultY*(float)77.14);
                ball.setLocation(resultX*MainActivity.accumulationX, resultY*MainActivity.accumulationY);
                Log.i("yunjae", "x = " + resultX + " y = " + resultY);
            }
        }

        Log.i("NearestPoint", "rsult = "+resultX+", "+resultY);


    }

    public double pointTopointDistance(double x1,double y1,double x2,double y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }



    public int getTxPower(int rssi) {
        txPower = -62;
        /*if(rssi >= -60)
            txPower = -59;
        else if(-61 <= rssi && rssi >= -68)
            txPower = -64;
        else if(-69 <= rssi && rssi >= -72)
            txPower = -57;*/
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }
}
