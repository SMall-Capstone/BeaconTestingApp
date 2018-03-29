package org.androidtown.beacontest2;

/**
 * Created by HANEUL on 2018-03-08.
 */

public class RssiItem {

    private int rssi;
    private String beaconID;
    private Double distance,distance1,distance2,distance3;
    private int doubleFilteredRssi;
    private int sungmoonFilteredRSSI;
    private int noFilterdRssi;

/*beaconInfo.getFilteredRSSIvalue(),
        beaconInfo.getName(),
        beaconInfo.getDistance(),
    resultX,
    resultY,
    doubleFilteredRSSI,
    distance1,
    rssi,
    distance2,
    sungmoonFilteredRSSI,
    distance3*/
    public RssiItem(int rssi, String beaconID, Double distance, int doubleFilteredRssi,double distance1,int noFilterdRssi,double distance2,int sungmoonFilteredRSSI,double distance3) {
        this.rssi = rssi;
        this.beaconID = beaconID;
        this.distance = distance;
        this.distance1 = distance1;
        this.distance2 = distance2;
        this.distance3 = distance3;
        this.doubleFilteredRssi = doubleFilteredRssi;
        this.noFilterdRssi = noFilterdRssi;
        this.sungmoonFilteredRSSI = sungmoonFilteredRSSI;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
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

    public int getDoubleFilteredRssi() {
        return doubleFilteredRssi;
    }

    public void setDoubleFilteredRssi(int doubleFilteredRssi) {
        this.doubleFilteredRssi = doubleFilteredRssi;
    }

    public int getNoFilterdRssi() {
        return noFilterdRssi;
    }

    public void setNoFilterdRssi(int noFilterdRssi) {
        this.noFilterdRssi = noFilterdRssi;
    }

    public Double getDistance1() {
        return distance1;
    }

    public void setDistance1(Double distance1) {
        this.distance1 = distance1;
    }

    public Double getDistance2() {
        return distance2;
    }

    public void setDistance2(Double distance2) {
        this.distance2 = distance2;
    }

    public Double getDistance3() {
        return distance3;
    }

    public void setDistance3(Double distance3) {
        this.distance3 = distance3;
    }

    public int getSungmoonFilteredRSSI() {
        return sungmoonFilteredRSSI;
    }

    public void setSungmoonFilteredRSSI(int sungmoonFilteredRSSI) {
        this.sungmoonFilteredRSSI = sungmoonFilteredRSSI;
    }
}
