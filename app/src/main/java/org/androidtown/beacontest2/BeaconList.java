package org.androidtown.beacontest2;

import android.util.Log;

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
    private int txPower;

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

    public ArrayList<BeaconInfo> findNearestBeacons(){
        ArrayList<BeaconInfo> beaconInfos = new ArrayList<BeaconInfo>();

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

            return beaconInfos;
        }
        else {
            Log.i("Sort","beaconInfos setting fail");
            return beaconInfos;
        }

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
