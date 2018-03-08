package org.androidtown.beacontest2;

/**
 * Created by HANEUL on 2018-03-08.
 */

public class RssiItem {

    private int rssi;
    private String beaconID;

    public RssiItem(int rssi, String beaconID) {
        this.rssi = rssi;
        this.beaconID = beaconID;
    }

    public String getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(String beaconID) {
        this.beaconID = beaconID;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }




}
