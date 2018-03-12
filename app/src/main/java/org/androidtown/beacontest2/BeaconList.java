package org.androidtown.beacontest2;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
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

    private BeaconList() {
        beaconInfoHashMap.put("MiniBeacon12802",new BeaconInfo("MiniBeacon12802","40001","12802"));
        beaconInfoHashMap.get("MiniBeacon12802").setLocation(0,0);

        beaconInfoHashMap.put("MiniBeacon12928",new BeaconInfo("MiniBeacon12928","40001","12928"));
        beaconInfoHashMap.get("MiniBeacon12928").setLocation(5,0);

        beaconInfoHashMap.put("MiniBeacon13298",new BeaconInfo("MiniBeacon13298","40001","13298"));
        beaconInfoHashMap.get("MiniBeacon13298").setLocation(4,3);

        beaconInfoHashMap.put("MiniBeacon_14863",new BeaconInfo("MiniBeacon_14863","40001","14863"));
        beaconInfoHashMap.put("MiniBeacon_14990",new BeaconInfo("MiniBeacon_14990","40001","14990"));
        beaconInfoHashMap.put("MiniBeacon_14997",new BeaconInfo("MiniBeacon_14997","40001","14997"));


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

    public static BeaconList getInstance(){
        return beaconList;
    }

    public ArrayList<BeaconInfo> findNearestBeacons(){
        ArrayList<BeaconInfo> beaconInfos = new ArrayList<BeaconInfo>();

        for(int i=0;i<beaconInfoHashMap.size();i++){
            beaconInfos.add(beaconInfoHashMap.get(beaconId.get(i)));
        }

        if(beaconInfos.size()==6/*==beaconInfoArrayList.size()*/){

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
}
