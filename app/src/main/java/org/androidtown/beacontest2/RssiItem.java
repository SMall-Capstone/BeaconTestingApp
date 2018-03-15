package org.androidtown.beacontest2;

/**
 * Created by HANEUL on 2018-03-08.
 */

public class RssiItem {

    private int rssi;
    private String beaconID;
    private Double distance;

    private Double resultX;
    private Double resultY;


    public RssiItem(int rssi, String beaconID, Double distance, Double resultX, Double resultY) {
        this.rssi = rssi;
        this.beaconID = beaconID;
        this.distance = distance;
        this.resultX = resultX;
        this.resultY = resultY;
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

    public Double getResultX() {
        return resultX;
    }

    public void setResultX(Double resultX) {
        this.resultX = resultX;
    }

    public Double getResultY() {
        return resultY;
    }

    public void setResultY(Double resultY) {
        this.resultY = resultY;
    }



}
