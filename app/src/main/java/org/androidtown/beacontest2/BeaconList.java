package org.androidtown.beacontest2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 이예지 on 2018-03-07.
 */

public class BeaconList {
    HashMap<String,BeaconInfo> beaconInfoArrayList = new HashMap<String,BeaconInfo>();
    private static BeaconList beaconList = new BeaconList();//싱글톤 패턴 적용

    private BeaconList() {
        beaconInfoArrayList.put("MiniBeacon12802",new BeaconInfo("MiniBeacon12802","40001","12802"));
        beaconInfoArrayList.put("MiniBeacon12928",new BeaconInfo("MiniBeacon12928","40001","12928"));
        beaconInfoArrayList.put("MiniBeacon13298",new BeaconInfo("MiniBeacon13298","40001","13298"));
        beaconInfoArrayList.put("MiniBeacon_14863",new BeaconInfo("MiniBeacon_14863","40001","14863"));
        beaconInfoArrayList.put("MiniBeacon_14990",new BeaconInfo("MiniBeacon_14990","40001","14990"));
        beaconInfoArrayList.put("MiniBeacon_14997",new BeaconInfo("MiniBeacon_14997","40001","14997"));
    }

    public BeaconInfo findBeacon(String name){
        return beaconInfoArrayList.get(name);
    }

    public static BeaconList getInstance(){
        return beaconList;
    }
}
